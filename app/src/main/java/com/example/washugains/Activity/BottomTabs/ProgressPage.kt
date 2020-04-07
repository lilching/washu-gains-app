package com.example.washugains.Activity.BottomTabs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.R
import kotlinx.android.synthetic.main.progress_page.*

class ProgressPage : AppCompatActivity() {

    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_page)
    }

    override fun onStart() {
        super.onStart()

        //grabs element from progress_page
        infoButton = progressInfo
        progressButton = progressProgress
        addButton = progressAdd

        infoButton.setOnClickListener {
            val intent = Intent(this, InfoPage::class.java)
            startActivity(intent)
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddPage::class.java)
            startActivity(intent)
        }
    }

}