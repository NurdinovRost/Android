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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

    class UserViewHolder(val root: View,
                         val callbackphone: (Contact) -> Unit,
                         val callbackmessage: (Contact) -> Unit): RecyclerView.ViewHolder(
        root
    ) {
        fun bind(user: Contact) {
            with(root) {
                first_name.text = user.name
                last_name.text = user.phoneNumber
                img.setImageResource(user.imageID)
                img_phone.setOnClickListener{
                    callbackphone(user)
                }
                img_message.setOnClickListener {
                    callbackmessage(user)
                }
            }
        }
    }
    

    class UserAdapter(
        usersOnInit: List<Contact>,
        private val onClickPhone: (Contact) -> Unit,
        private val onClickMessage: (Contact) -> Unit,
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
                callbackphone = onClickPhone,
                callbackmessage = onClickMessage
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
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS),
            1
        );
//        val userList = fetchAllContacts()
        val viewManager = GridLayoutManager(this, 2)
        contactsAdapter = UserAdapter(emptyList(), {
            val intentPhone = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phoneNumber}"))
            startActivity(intentPhone)
        }, {
            val intentMessage = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${it.phoneNumber}"))
            intentMessage.putExtra("sms_body", "Welcome to the club")
            startActivity(intentMessage)
        })
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
                grantResults.forEachIndexed { index, result ->
                    when (permissions[index]) {
                        Manifest.permission.READ_CONTACTS -> {
                            if (result == PackageManager.PERMISSION_GRANTED) {
                                contactsAdapter.users = fetchAllContacts()
                                val temp = contactsAdapter.users.size
                                val message = this.getResources()
                                    .getQuantityString(R.plurals.plur, temp, temp)
                                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    R.string.message_error,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        Manifest.permission.SEND_SMS -> {
                            if (result == PackageManager.PERMISSION_DENIED) {
                                Toast.makeText(
                                    this@MainActivity,
                                    R.string.message_error,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
}