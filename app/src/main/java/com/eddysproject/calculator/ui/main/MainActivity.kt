package com.eddysproject.calculator.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eddysproject.calculator.R
import com.eddysproject.calculator.databinding.ActivityMainBinding
import com.eddysproject.calculator.db.data.History
import com.eddysproject.calculator.extension.observe
import com.eddysproject.calculator.ui.main.MainViewModel.Companion.DIVISION
import com.eddysproject.calculator.ui.main.MainViewModel.Companion.DOT
import com.eddysproject.calculator.ui.main.MainViewModel.Companion.MINUS
import com.eddysproject.calculator.ui.main.MainViewModel.Companion.MULTIPLY
import com.eddysproject.calculator.ui.main.MainViewModel.Companion.PLUS
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val fragment = SecondFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.apply {
            observe(data, ::display)
            observe(history, ::updateHistory)
            observe(histories, ::updateHistories)
        }

        binding.ac.setOnClickListener {
            viewModel.onAc()
        }
        binding.clearHistory.setOnClickListener {
            viewModel.onClearHistory()
            fragment.clearView()
        }
        binding.back.setOnClickListener {
            viewModel.onBack()
        }

        binding.zero.setOnClickListener { addNum("0") }
        binding.one.setOnClickListener { addNum("1") }
        binding.two.setOnClickListener { addNum("2") }
        binding.three.setOnClickListener { addNum("3") }
        binding.four.setOnClickListener { addNum("4") }
        binding.four.setOnClickListener { addNum("4") }
        binding.five.setOnClickListener { addNum("5") }
        binding.six.setOnClickListener { addNum("6") }
        binding.seven.setOnClickListener { addNum("7") }
        binding.eight.setOnClickListener { addNum("8") }
        binding.nine.setOnClickListener { addNum("9") }

        binding.dot.setOnClickListener {
            addDot(DOT)
        }
        binding.plus.setOnClickListener {
            addOperation(PLUS)
        }
        binding.minus.setOnClickListener {
            addOperation(MINUS)
        }
        binding.multiply.setOnClickListener {
            addOperation(MULTIPLY)
        }
        binding.divis.setOnClickListener {
            addOperation(DIVISION)
        }
        binding.equal.setOnClickListener {
            viewModel.onEqual()
        }

        viewModel.getHistories()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment)
        transaction.commit()
    }

    private fun addNum(s: String) {
        viewModel.addNum(s)
    }

    private fun addOperation(s: String) {
        viewModel.addOperation(s)
    }

    private fun addDot(s: String) {
        viewModel.addDot(s)
    }

//    private fun hasNotBeenProgrammed(s: String) {
//        val intent = Intent(this, SecondActivity::class.java)
//        intent.putExtra(SecondActivity.WRONG_BUTTON, s)
//        startActivity(intent)
//    }

    private fun display(value: String?) {
        binding.displayText.text = value
    }

    private fun updateHistory(value: String?) {
        value?.let { fragment.addToList(it) }
    }

    private fun updateHistories(value: List<History>?) {
        value?.forEach {
            fragment.addToList(it.result)
        }
    }
}