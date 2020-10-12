package com.eddysproject.calculator.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eddysproject.calculator.db.data.History
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder


class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private var displayText: String = ZERO
    private var lastOperation = EMPTY
    private var countDots = 0

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data

    private val _history = MutableLiveData<String>()
    val history: LiveData<String> = _history

    private val _histories = MutableLiveData<List<History>>()
    val histories: LiveData<List<History>> = _histories

    fun addOperation(s: String) {

        if (lastOperation.isNotEmpty()) {
                checkDisplayText(displayText)
                lastOperation = s
            if (displayText.last().isDigit()) {
                displayText += s
                countDots = 1
            } else {
                displayText = displayText.dropLast(1) + s
            }
        }
        else {
            if (displayText.last().toString() == DOT)
                displayText += ZERO
            displayText += s
            lastOperation = s
            countDots = 1
        }
        _data.value = displayText
    }

    fun onEqual() {
        if (lastOperation.isNotEmpty() && displayText.last().toString() != lastOperation)
            checkDisplayText(displayText)
//        Log.d("LOP", "lastOperation: $lastOperation")
//        Log.d("LOP", "countDots $countDots")
        _data.value = displayText
    }

    fun addNum(s: String) {
        if (displayText == ZERO) {
            displayText = displayText.dropLast(1) + s
        }
        else if (displayText.last().toString() == ZERO && displayText[displayText.length - 2].toString() == lastOperation) {
            displayText = displayText.dropLast(1) + s
        }
        else
            displayText += s
        _data.value = displayText
    }

    fun addDot(s: String) {
        if (!displayText.contains(DOT) && lastOperation.isEmpty()) {
            displayText += s
            countDots ++
        }
        else if (lastOperation.isNotEmpty() && countDots < 2) {
            if (!displayText.last().isDigit()) {
                displayText += "$ZERO$s"
                countDots ++
            } else {
                displayText += s
                countDots ++
            }
        }
//        Log.d("LOP", "countDots $countDots")
        _data.value = displayText
    }

    fun onBack() {
        if (displayText.length != 1) {
            if (!displayText.last().isDigit())
                countDots--
            displayText = displayText.dropLast(1)
        } else
            onAc()
        _data.value = displayText
    }

    fun onAc() {
        displayText = ZERO
        lastOperation = EMPTY
        countDots = 0
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

    private fun checkDisplayText(s: String) {
        try {
            val ex = ExpressionBuilder(s).build()
            val result = ex.evaluate()
            val longRes = result.toLong()
            if (result == longRes.toDouble()) {
                displayText = longRes.toString()
                countDots = 0
            }
            else {
                displayText = String.format("%.5f", result).toDouble().toString()
                countDots = 1
            }
            if (s.last().toString() == DOT)
                _history.value = "$s$ZERO=$displayText"
            else
                _history.value = "$s=$displayText"
            viewModelScope.launch {
                repository.insertAll(History("$s=$displayText"))
            }
        } catch (e: ArithmeticException) {
            Log.d("Error", "message: ${e.message}")
            displayText = ZERO
            //Toast.makeText(, "Division by zero!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.d("Error", "message: ${e.message}")
        }
        lastOperation = EMPTY
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
