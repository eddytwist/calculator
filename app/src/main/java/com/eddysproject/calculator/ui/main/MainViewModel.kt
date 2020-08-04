package com.eddysproject.calculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eddysproject.calculator.db.data.History
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private var displayText: String = EMPTY
    private var lastOperation = EMPTY

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data

    private val _history = MutableLiveData<String>()
    val history: LiveData<String> = _history

    private val _histories = MutableLiveData<List<History>>()
    val histories: LiveData<List<History>> = _histories

    fun addNum(s: String) {
        if (displayText == ZERO) {
            displayText = s
        } else {
            displayText += s
        }
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
        _data.value = displayText
    }

    fun addOperation(s: String) {
        val l = lastOperation
        lastOperation =
            if (l != EMPTY && displayText[displayText.length - 1].toString() != l) {
                checkText(displayText)
                EMPTY
            } else {
                if (l == PLUS || l == MINUS || l == MULTIPLY || l == DIVISION) {
                    displayText = displayText.dropLast(1)
                }
                displayText += s
                s
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

    fun onClearHistory() {
        viewModelScope.launch {
            repository.onClearHistory()
        }
    }

    private fun checkText(s: String) {
        var res = 0
        val firstArr: List<String>
        when (lastOperation) {
            PLUS -> {
                firstArr = s.split(PLUS)
                res = firstArr[0].toInt() + firstArr[1].toInt()
            }
            MINUS -> {
                firstArr = s.split(MINUS)
                res = firstArr[0].toInt() - firstArr[1].toInt()
            }
            MULTIPLY -> {
                firstArr = s.split(MULTIPLY)
                res = firstArr[0].toInt() * firstArr[1].toInt()
            }
            DIVISION -> {
                firstArr = s.split(DIVISION)
                res = if (firstArr[1] == ZERO) {
                    0
                } else firstArr[0].toInt() / firstArr[1].toInt()
            }
            else -> displayText += EMPTY
        }
        displayText = "$res"
        _history.value = "$s = $res"

        viewModelScope.launch {
            repository.insertAll(History("$s = $res"))
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