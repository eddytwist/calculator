package com.eddysproject.calculator.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eddysproject.calculator.db.data.History
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private var displayText: String = EMPTY
    private var lastOperation = EMPTY

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data

    private val _history = MutableLiveData<String>()
    val history: LiveData<String> = _history

    private val _histories = MutableLiveData<List<History>>()
    val histories: LiveData<List<History>> = _histories

    fun addOperation(s: String) {
        if (lastOperation.isNotEmpty()) {
                checkText(displayText)
                lastOperation = s
            if (displayText[displayText.length - 1].isDigit()) {
                displayText += s
            } else {
                displayText = displayText.dropLast(1) + s
            }
        }
        else {
            displayText += s
            lastOperation = s
        }
        _data.value = displayText
    }

    fun onEqual() {
        val last =
            displayText[displayText.length - 1].toString()
        if (last == PLUS || last == MINUS || last == MULTIPLY || last == DIVISION) {
            val allText = displayText
            displayText = allText + displayText.dropLast(1)
            checkText(displayText)
            lastOperation =
                EMPTY
        } else {
            checkText(displayText)
            lastOperation =
                EMPTY
        }
        _data.value = displayText
    }

    private fun checkText(s: String) {
        try {
            val ex = ExpressionBuilder(s).build()
            val result = ex.evaluate()
            val longRes = result.toLong()
            if (result == longRes.toDouble())
                displayText = longRes.toString()
            else {
                displayText = String.format("%.5f", result).toDouble().toString()
            }
            _history.value = "$s = $displayText"
            viewModelScope.launch {
                repository.insertAll(History("$s = $displayText"))
            }
        } catch (e: ArithmeticException) {
            Log.d("Error","message: ${e.message}")
            displayText = ZERO
//            val toast = Toast.makeText(,)
//            "Division by zero!", Toast.LENGTH_SHORT
//            toast.setGravity(Gravity.CENTER, 0, 0)
//            toast.show()
        } catch (e: Exception) {
            Log.d("Error","message: ${e.message}")
        }
    }

    fun addNum(s: String) {
        if (displayText == ZERO) {
            displayText = s
        } else {
            if (displayText[displayText.length - 1].toString() == ZERO && !displayText[displayText.length - 2].isDigit() && displayText[displayText.length - 2].toString() != DOT)
                displayText += ".$s"
            else
                displayText += s
        }
        _data.value = displayText
    }

    fun addDot(s: String) {
        displayText += s
        _data.value = displayText
    }

    fun onBack() {
        displayText = if (displayText.length != 1) {
            displayText.dropLast(1)
        } else {
            ZERO
        }
        _data.value = displayText
    }

    fun onAc() {
        displayText = ZERO
        lastOperation = EMPTY
        _data.value = displayText
    }

    fun onClearHistory() {
        viewModelScope.launch {
            repository.onClearHistory()
        }
    }

    fun getHistories() {
        viewModelScope.launch {
            _histories.value = repository.getAll()
        }
    }

    companion object {
        const val EMPTY = ""
        const val ZERO = "0"

        const val DOT = "."
        const val PLUS = "+"
        const val MINUS = "-"
        const val MULTIPLY = "*"
        const val DIVISION = "/"
    }
}