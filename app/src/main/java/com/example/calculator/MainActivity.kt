package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    var textView : TextView? = null
    var lastIsNumeric : Boolean = true
    var lastIsDecimalPoint : Boolean = true
    var decimalInNumber : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tv_screen)
    }

    fun onDigit(view : View){
        textView?.append((view as Button).text)
        lastIsDecimalPoint = false
        lastIsNumeric = true
    }

    fun onClear(view:View){
        textView?.text = ""
        decimalInNumber = false
    }

    fun onDecimalPoint(view:View){
        if(lastIsNumeric && !lastIsDecimalPoint && !decimalInNumber){
            textView?.append(".")
            lastIsNumeric = false
            lastIsDecimalPoint = true
            decimalInNumber = true
        }
    }

    fun onOperator(view : View){
        //let is a safe call symbol
        textView?.text?.let{
            //If an operator is added then it should be impossible to append another operator symbol
            if(lastIsNumeric && !isOperatorAdded(it.toString())){
                textView?.append((view as Button).text)
                lastIsNumeric = false
                lastIsDecimalPoint = false
                decimalInNumber = false
            }
        }
    }

    fun onEqual(view : View){
        if(lastIsNumeric){
            var equation = textView?.text.toString()
            //Remove the beginning minus sign
            var prefix = ""
            try{
                if(equation.startsWith("-")){
                    prefix="-"
                    equation = equation.substring(1)
                }
                //Subtraction sector
                if(equation.contains("-")){
                    val splitValues = equation.split("-")
                    var one = splitValues[0]
                    val two = splitValues[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    val result = one.toDouble() - two.toDouble()
                    textView?.text = removeZero(result.toString())
                }
                //Addition sector
                if(equation.contains("+")){
                    val splitValues = equation.split("+")
                    val one = splitValues[0]
                    val two = splitValues[1]
                    val result = one.toDouble() + two.toDouble()
                    textView?.text = removeZero(result.toString())
                }
                //Multiplication sector
                if(equation.contains("X")){
                    val splitValues = equation.split("X")
                    val one = splitValues[0]
                    val two = splitValues[1]
                    val result = one.toDouble() * two.toDouble()
                    textView?.text = removeZero(result.toString())
                }
                //Division sector
                if(equation.contains("/")){
                    val splitValues = equation.split("/")
                    val one = splitValues[0]
                    val two = splitValues[1]
                    if(two.toDouble() == 0.0){
                        val result = "Division by zero error"
                        textView?.text = result
                    }else{
                        val result = one.toDouble() / two.toDouble()
                        textView?.text = removeZero(result.toString())
                    }
                }
            }catch(e : ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    private fun  isOperatorAdded(value : String) : Boolean{
        //Ignore the minus sign that represents negative numbers
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("/") || value.contains("*") || value.contains("+") || value.contains("-")
        }
    }

    private fun removeZero(result : String) : String{
        var value = result
        Log.e("NumberZ",result)
        if(value.contains(".0")){
            value = result.substring(0,result.length - 2)
        }
        return value
    }
}