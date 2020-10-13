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
    private var countDecs = 0

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
                countDecs = 1
            } else {
                displayText = displayText.dropLast(1) + s
            }
        }
        else {
            if (displayText.last().toString() == DECIMAL_POINT)
                displayText += ZERO
            displayText += s
            lastOperation = s
            countDecs = 1
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
        if (!displayText.contains(DECIMAL_POINT) && lastOperation.isEmpty()) {
            displayText += s
            countDecs ++
        }
        else if (lastOperation.isNotEmpty() && countDecs < 2) {
            if (!displayText.last().isDigit()) {
                displayText += "$ZERO$s"
                countDecs ++
            } else {
                displayText += s
                countDecs ++
            }
        }
//        Log.d("LOP", "countDots $countDots")
        _data.value = displayText
    }

    fun onBack() {
        if (displayText.length != 1) {
            if (!displayText.last().isDigit())
                countDecs--
            displayText = displayText.dropLast(1)
        } else
            onAc()
        _data.value = displayText
    }

    fun onAc() {
        displayText = ZERO
        lastOperation = EMPTY
        countDecs = 0
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
                countDecs = 0
            }
            else {
                displayText = String.format("%.5f", result)
                countDecs = 1
            }
            if (s.last().toString() == DECIMAL_POINT)
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

        const val DECIMAL_POINT = "."
        const val ADDITION = "+"
        const val SUBTRACTION = "-"
        const val MULTIPLICATION = "*"
        const val DIVISION = "/"
    }
}
