package com.project.calculator

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.calculator.databinding.ActivitySecondBinding
import kotlinx.android.synthetic.main.activity_second.*


class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        showBut()

        binding.back.setOnClickListener {
            val intent =  Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showBut(){
        val wrongBut = intent.getStringExtra(WRONG_BUTTON)
        textAfterDot.text = getString(R.string.wrong_button, wrongBut)
    }

    override fun onStop() {
        super.onStop()
        val toast = Toast.makeText(applicationContext, "Please, use only INTEGER values.", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    companion object {
        const val WRONG_BUTTON = "WRONG_BUTTON"
        const val SECOND_ACTIVITY = "SECOND_ACTIVITY"
    }
}
