package com.example.washugains.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.example.washugains.Activity.BottomTabs.AddPage
import com.example.washugains.Activity.BottomTabs.InfoPage
import com.example.washugains.Activity.BottomTabs.ProgressPage
import com.example.washugains.R
import kotlinx.android.synthetic.main.welcome_page.*



class WelcomePage : AppCompatActivity() {

  //  private lateinit var db : FirebaseFirestore
    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)

    }

    override fun onStart() {
        super.onStart()
      //  val dailyInfoList=intent.getParcelableArrayListExtra<DailyInfo>("dailyInfoList")

        val username=intent?.getStringExtra("username")
        val calories=intent?.getStringExtra("calories")
        if (username != null) {
          usernameText.text = username
        }
        //grabs element from welcome_page
        infoButton = welcomeInfo
        progressButton = welcomeProgress
        addButton = welcomeAdd

        infoButton.setOnClickListener {
            val intent = Intent(this, InfoPage::class.java)
            intent.putExtra("username", username)
            intent.putExtra("calories", calories)
            startActivity(intent)
        }

        progressButton.setOnClickListener {
            val intent = Intent(this, ProgressPage::class.java)
           // intent.putExtra("dailyInfoList",dailyInfoList)
            startActivity(intent)
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddPage::class.java)
            startActivity(intent)
        }
    }
}
