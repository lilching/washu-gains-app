package com.example.washugains.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.R
import kotlinx.android.synthetic.main.activity_main.*

class AddPage : AppCompatActivity() {

    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        //grabs element from activity_main
        infoButton = addInfo
        progressButton = addProgress
        addButton = addAdd

        infoButton.setOnClickListener {
            val intent = Intent(this, InfoPage::class.java)
            startActivity(intent)
        }

        progressButton.setOnClickListener {
            val intent = Intent(this, ProgressPage::class.java)
            startActivity(intent)
        }
    }

}