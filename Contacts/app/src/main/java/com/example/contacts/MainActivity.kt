package com.example.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*


class MainActivity : AppCompatActivity() {

    data class Contact(val name: String, val phoneNumber: String, val imageID: Int)

    lateinit var contactsAdapter: UserAdapter

    private fun fetchAllContacts(): List<Contact> {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
                .use { cursor ->
                    if (cursor == null) return emptyList()
                    val builder = ArrayList<Contact>()

                    while (cursor.moveToNext()) {
                        val name =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) ?: "N/A"
                        val phoneNumber =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: "N/A"
                        val imageID = listOf(R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4).random()

                        builder.add(Contact(name, phoneNumber, imageID))
                    }
                    return builder
                }
    }

    class UserViewHolder(val root: View, val callback: (Contact) -> Unit): RecyclerView.ViewHolder(
        root
    ) {
        fun bind(user: Contact) {
            with(root) {
                setOnClickListener {
                    callback(user)
                }
                first_name.text = user.name
                last_name.text = user.phoneNumber
                img.setImageResource(user.imageID)
            }
        }
    }

    class UserAdapter(
        usersOnInit: List<Contact>,
        private val onClick: (Contact) -> Unit
    ): RecyclerView.Adapter<UserViewHolder>() {

        var users = usersOnInit
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            val holder = UserViewHolder(
                root = view,
                callback = onClick
            )
            return holder
        }

        override fun onBindViewHolder(
            holder: UserViewHolder,
            position: Int
        ) = holder.bind(users[position])

        override fun getItemCount() = users.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            1
        );
//        val userList = fetchAllContacts()
        val viewManager = LinearLayoutManager(this)
        contactsAdapter = UserAdapter(emptyList()) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + it.phoneNumber))
            startActivity(intent)
        }
        RW.apply {
            layoutManager = viewManager
            adapter = contactsAdapter
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    contactsAdapter.users = fetchAllContacts()
                    val temp = contactsAdapter.users.size
                    val message = this.getResources().getQuantityString(R.plurals.plur, temp, temp)
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this@MainActivity, R.string.message_error, Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }
}