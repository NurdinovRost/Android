package com.example.calculator

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.content_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(findViewById(R.id.toolbar))
//
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        label_input.isSelected = true
        btn_0.setOnClickListener { setTextFields("0") }
        btn_1.setOnClickListener { setTextFields("1") }
        btn_2.setOnClickListener { setTextFields("2") }
        btn_3.setOnClickListener { setTextFields("3") }
        btn_4.setOnClickListener { setTextFields("4") }
        btn_5.setOnClickListener { setTextFields("5") }
        btn_6.setOnClickListener { setTextFields("6") }
        btn_7.setOnClickListener { setTextFields("7") }
        btn_8.setOnClickListener { setTextFields("8") }
        btn_9.setOnClickListener { setTextFields("9") }
        btn_float.setOnClickListener { setTextFields(".") }
        btn_open_bracket.setOnClickListener { setTextFields("(") }
        btn_close_bracket.setOnClickListener { setTextFields(")") }
        btn_division.setOnClickListener { setTextFields("/") }
        btn_multi.setOnClickListener { setTextFields("*") }
        btn_plus.setOnClickListener { setTextFields("+") }
        btn_minus.setOnClickListener { setTextFields("-") }

        btn_AC.setOnClickListener {
            label_input.text = ""
            label_result.text = ""
        }

        btn_clear.setOnClickListener {
            val str = label_input.text.toString()
            if(str.isNotEmpty())
                label_input.text = str.substring(0, str.length - 1)

            label_result.text = ""
        }

        btn_equally.setOnClickListener {
            try {

                val ex = ExpressionBuilder(label_input.text.toString()).build()
                val result = ex.evaluate()
                val longRes = result.toLong()
                Log.d("res", ": $result and ${longRes.toDouble()}")
                if(result == longRes.toDouble()) {
                    label_result.text =
                        longRes.toBigDecimal().setScale(15, RoundingMode.HALF_UP).toLong().toString()
                }
                else {
                    label_result.text =
                        result.toBigDecimal().setScale(14, RoundingMode.HALF_UP).toDouble().toString()
                }

            } catch (e:Exception) {
                Log.d("Ошибка", "сщщбщение: ${e.message}")
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.run {
            putString("label_input", label_input.text.toString())
            putString("label_result", label_result.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        label_input.text = savedInstanceState.getString("label_input")
        label_result.text = savedInstanceState.getString("label_result")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setTextFields(str: String) {
        if (label_result.text.isNotEmpty()) {
            label_input.text = label_result.text
            label_result.text = ""
        }
//        if (checkInputLabel(str))
        label_input.append(str)
    }

//    fun checkInputLabel(str: String): Boolean {
//        var flag: Boolean = true
//        val set = setOf("+", "-", "*", "/")
//        if (str == ".") {
//            val p = label_input.text.indexOf(".", 0)
//            if (p != -1)
//                flag = false
//            if (label_input.text.isEmpty())
//                flag = false
//        }
//        if (set.contains(str)) {
//            if (label_input.text.isEmpty() && str != "-")
//                flag = false
//            if (label_input.text.isNotEmpty() && set.contains(label_input.text.last().toString())) {
//                flag = false
//            } else if (label_input.text.last().toString() == ".") {
//                    flag = false
//            }
//        }
//
//        return flag
//    }
}