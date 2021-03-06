package com.example.calculator

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

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
                val array = parseExpression(label_input.text.toString())
                val queue = convertFromInfixToPostfix(array)
                val result = calculatePostfixResult(queue)
                label_result.text = result.toString()
            } catch (e:Exception) {
                Log.d("msg", "$e")
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
        if (checkInputLabel(str))
            label_input.append(str)
    }


    fun checkInputLabel(str: String): Boolean {
        val operations = setOf("+", "-", "*", "/")
        val brackets = setOf("(", ")")
        val numerals = setOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

        if (str == ".") {
            if (label_input.text.isEmpty())
                return false

            val temp: String = label_input.text.toString().split('+', '-', '*', '/', '(', ')').last()
            val p = temp.indexOf(".", 0)
            if (p != -1 || temp.isEmpty())
                return false
        }
        if (operations.contains(str)) {
            if (label_input.text.isEmpty())
                return str == "-"
            else if (label_input.text.isNotEmpty() && operations.contains(label_input.text.last().toString())) {
                return false
            } else if (label_input.text.last().toString() == "." || label_input.text.last().toString() == "(") {
                return false
            }
        }

        if (brackets.contains(str)) {
//            var stack = ArrayDeque<Int>()
            if (str=="(") {
                if (label_input.text.isEmpty())
                    return true
                else if (label_input.text.last().toString() == "." || label_input.text.last().toString() == ")")
                    return false
                else if (numerals.contains(label_input.text.last().toString()))
                    return false
            } else {
                val openBrackets = countElem(label_input.text.toString(), '(')
                val closeBrackets = countElem(label_input.text.toString(), ')')

                when {
                    label_input.text.isEmpty() -> return false
                    openBrackets <= closeBrackets -> return false
                    operations.contains(label_input.text.last().toString()) -> return false
                    label_input.text.toString() == "." -> return false
                    label_input.text.toString() == ")" -> return false
                }
            }
        }

        if (numerals.contains(str)) {
            when {
                label_input.text.isEmpty() -> return true
                label_input.text.last().toString() == ")" -> return false
            }
        }
        return true
    }

    fun countElem(s: String, c: Char): Int {
        var sum = 0
        for (element in s) {
            if (element == c) sum++
        }
        return sum
    }

    fun parseExpression(str: String): ArrayList<String> {
        var array = arrayListOf<String>()
        val operations = setOf('+', '-', '*', '/')
        val brackets = setOf('(', ')')
        val numerals = setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        var temp = ""
        str.forEachIndexed {index, it ->
            when {
                numerals.contains(it) -> {
                    temp += it
                }
                it == '.' -> temp += it
                operations.contains(it) ||  brackets.contains(it)-> {
                    if (index == 0 && it == '-' && temp.isEmpty()) {
                        temp += "-"
                    } else {
                        if (temp.isNotEmpty())
                            array.add(temp)
                        temp = ""
                        array.add(it.toString())
                    }
                }
            }
        }
        if (temp.isNotEmpty())
            array.add(temp)
        Log.d("!?!?", "$array")
        return array
    }

    fun convertFromInfixToPostfix(array: ArrayList<String>): Queue<String> {
        val operations = setOf("+", "-", "*", "/")
        val brackets = setOf("(", ")")
        val queueNumbers: Queue<String> = ArrayDeque<String>()
        val stackOperations: Stack<String> = Stack()
        array.forEach {
            if (operations.contains(it)) {
                if (stackOperations.isEmpty() || stackOperations.lastElement() == "(")
                    stackOperations.push(it)
                else if (it == "*" || it == "/") {
                    while (stackOperations.size > 0 && (stackOperations.lastElement() == "*" || stackOperations.lastElement() == "/" )) {
                        val operation = stackOperations.pop()
                        queueNumbers.add(operation)
                    }
                    stackOperations.push(it)
                }
                else if (it == "+" || it == "-") {
                    while (stackOperations.size > 0 && stackOperations.lastElement() != "(") {
                        val operation = stackOperations.pop()
                        queueNumbers.add(operation)
                    }
                    stackOperations.push(it)
                }

            }
            else if (brackets.contains(it)) {
                if (it == "(")
                    stackOperations.push(it)
                else {
                    while (stackOperations.size > 0 && stackOperations.lastElement() != "(") {
                        val operation = stackOperations.pop()
                        queueNumbers.add(operation)
                    }
                    if (stackOperations.lastElement() == "(")
                        stackOperations.pop()
                }
            } else {
                queueNumbers.add(it)
            }
        }
        while (stackOperations.isNotEmpty()) {
            val elem = stackOperations.pop()
            queueNumbers.add(elem)
        }
        return queueNumbers
    }


    fun calculatePostfixResult(queue: Queue<String>): BigDecimal {
        var stack: Stack<BigDecimal> = Stack()
        val operations = setOf("+", "-", "*", "/")
        while (queue.isNotEmpty()) {
            val elem: String = queue.remove()
            if (operations.contains(elem)) {
                val a: BigDecimal = stack.pop()
                val b: BigDecimal = stack.pop()
                when (elem) {
                    "+" -> stack.push(a + b)
                    "-" -> stack.push(b - a)
                    "*" -> stack.push(a * b)
                    "/" -> stack.push((b.toDouble() / a.toDouble()).toBigDecimal())
                }
            } else {
                stack.push(elem.toBigDecimal())
            }
        }

        return stack.lastElement()
    }


}