package com.example.washugains.Activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.Activity.BottomTabs.AddPage
import com.example.washugains.R
import kotlinx.android.synthetic.main.input_page.*

class InputPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_page)
    }


    override fun onStart() {
        super.onStart()
        val username = intent?.getStringExtra("username")
        val calories = intent?.getStringExtra("calories")


        continueButton.setOnClickListener{
            var height = heightInput.text.toString()
            var weight = weightInput.text.toString()

            if (height != "" && weight != "") {
                val intent = Intent(this, WelcomePage::class.java)
                intent.putExtra("username", username)
                intent.putExtra("calories", calories)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
