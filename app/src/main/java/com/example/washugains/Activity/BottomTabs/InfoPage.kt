package com.example.washugains.Activity.BottomTabs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.Activity.LandingPage
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.info_page.*

class InfoPage : AppCompatActivity() {

    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button

    private lateinit var logoutButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_page)
    }

    override fun onStart() {
        super.onStart()

        //grab element from info_page
        infoButton = infoInfo
        progressButton = infoProgress
        addButton = infoAdd
        logoutButton = logout

        progressButton.setOnClickListener {
            val intent = Intent(this, ProgressPage::class.java)
            startActivity(intent)
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddPage::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }
    }
}