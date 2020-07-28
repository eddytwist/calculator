package com.eddysproject.calculator


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eddysproject.calculator.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var lastOperation = ""
    private lateinit var binding: ActivityMainBinding

    private val fragment = SecondFragment()
    private val db = CalculatorApplication.db
    //private val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "history-db").allowMainThreadQueries().build()
    private var histories = db?.historyDao()?.getAll()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        histories?.forEach {
            fragment.addToList(it.result)
        }

        val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.container, fragment)
            transaction.commit()

            binding.ac.setOnClickListener {
                displayText.text = "0"}
            binding.clearHistory.setOnClickListener {
                db?.clearAllTables()
                fragment.clearView()
            }
            binding.back.setOnClickListener {
                if (displayText.text.toString().length != 1) {
                    displayText.text = displayText.text.toString().dropLast(1)
                } else displayText.text = "0"
            }
            binding.dot.setOnClickListener {
                hasNotBeenProgrammed(".")
//                if (lastOperation != ".") {
//                    binding.displayText.append(".")
//                    lastOperation = "."
//                } else binding.displayText.append("")
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
            binding.plus.setOnClickListener {
                //Log.d("TAG", displayText.text.toString())
                //Log.d("TAG", "$lastOperation")
                addOperation("+")
            }
            binding.minus.setOnClickListener {
                addOperation("-")
            }
            binding.mult.setOnClickListener {
                addOperation("*")
            }
            binding.divis.setOnClickListener {
                addOperation("/")
            }
            binding.equal.setOnClickListener {
                val last = displayText.text.toString()[displayText.text.toString().length-1].toString()
                if(last == "+" || last == "-" || last == "*" || last == "/") {
                    val allText = displayText.text.toString()
                    displayText.text = allText + displayText.text.toString().dropLast(1)
                    checkText(displayText.text.toString())
                    lastOperation = ""
                }
                else {
                    checkText(displayText.text.toString())
                    lastOperation = ""
                }
            }
    }

    private fun checkText (s : String) {
        var res = 0
        val firstArr : List<String>
        when (lastOperation)
        {
            "+" -> {
                firstArr = s.split("+")
                res = firstArr[0].toInt() + firstArr[1].toInt()
                //Log.d("TAG", "$lastOperation")
            }
            "-" -> {
                firstArr = s.split("-")
                res = firstArr[0].toInt() - firstArr[1].toInt()
            }
            "*" -> {
                firstArr = s.split("*")
                res = firstArr[0].toInt() * firstArr[1].toInt()
            }
            "/" -> {
                firstArr = s.split("/")
                res = if(firstArr[1] == "0") {
                    0
                } else firstArr[0].toInt() / firstArr[1].toInt()
            }
            else -> displayText.append("")
        }
        displayText.text = "$res"

        fragment.addToList("$s = $res")
        db?.historyDao()?.insertAll(History("$s = $res"))

    }

    private fun addNum(s : String) {
        if(displayText.text.toString() == "0") {
            displayText.text = s
        }
        else binding.displayText.append(s)
    }
    private fun addOperation(s : String) {
        val l = lastOperation
        lastOperation = if(l != "" && displayText.text.toString()[displayText.text.toString().length-1].toString() != l) {
            checkText(displayText.text.toString())
            ""
        } else {
            if (l == "+" || l == "-" || l == "*" || l == "/") {
                displayText.text = displayText.text.toString().dropLast(1)
            }
            binding.displayText.append(s)
            s
        }
    }
    private fun hasNotBeenProgrammed(s : String) {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(SecondActivity.WRONG_BUTTON, s)
        startActivity(intent)
    }

    companion object{
        const val MAIN_PARCELABLE = "MAIN_PARCELABLE"
    }

}


