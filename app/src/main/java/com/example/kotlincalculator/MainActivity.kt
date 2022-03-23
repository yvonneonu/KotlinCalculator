package com.example.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var canAddOperator = false
    private var  canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View) {
        if (view is Button)
        {
            if (view.text == ".")
            {
                if (canAddDecimal)
                    workingTV.append(view.text)
                canAddDecimal = false
            }
            else
                 workingTV.append(view.text)
            canAddOperator = true

        }

    }

    fun operatorAction(view: View) {
        if (view is Button && canAddOperator)
        {
            workingTV.append(view.text)
            canAddOperator = false
            canAddDecimal = true
        }

    }
    fun allClearAction(view: View) {

        workingTV.text = ""
        resultsTV.text = ""
    }
    fun backSpaceAction(view: View) {
        val length = workingTV.length()
        if (length > 0)
            workingTV.text = workingTV.text.subSequence(0, length - 1)
    }
    fun equalsAxtion(view: View) {
        resultsTV.text = calculateResult()
    }

    private fun calculateResult(): String {

        val digitOperators = digitOperators()
        if (digitOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitOperators)

        if (timesDivision.isEmpty()) return ""

        val result = addSubstractCalculate(timesDivision)

        return result.toString()
    }

    private fun addSubstractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices){

            if (passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float

                if (operator == '+')
                    result += nextDigit

                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result

    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {

        var list = passedList

        while (list.contains('*') || list.contains('/')){
            list = calcTimesDiv(list)
        }

        return list

    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()

        var reStartIndex = passedList.size
        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex && i < reStartIndex)
            {
                val operate = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float

                when(operate){
                    '*' ->{
                        newList.add(prevDigit * nextDigit)
                        reStartIndex = i + 1
                    }

                    '/' ->{

                        newList.add(prevDigit / nextDigit)

                        reStartIndex = i + 1

                    }
                    else ->{
                        newList.add(prevDigit)
                        newList.add(operate)

                    }
                    
                }

            }

            if (i > reStartIndex)
                newList.add(passedList[i])

        }
        return newList
    }

    private fun digitOperators(): MutableList<Any>
    {

        val list = mutableListOf<Any>()

        var currentDigit = ""

        for (character in workingTV.text){
            if (character.isDigit() || character == '.' )
                currentDigit += character
                else
                {
                    list.add(currentDigit.toFloat())
                    currentDigit = ""
                    list.add(character)
                }

        }

        if (currentDigit != "")

            list.add(currentDigit.toFloat())

        return list
    }



}