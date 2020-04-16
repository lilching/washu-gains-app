package com.example.washugains.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
    private lateinit var inspireQuote : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)

    }

    override fun onStart() {
        super.onStart()
      //  val dailyInfoList=intent.getParcelableArrayListExtra<DailyInfo>("dailyInfoList")

        var quoteList = ArrayList<String>()
        //Walt Disney
        quoteList.add("The way to get started is to quit talking and begin doing")
        //Winston Churchill
        quoteList.add("The pessimist sees difficulty in every opportunity. The optimist sees the " +
                "opportunity in every difficulty")
        //Will Rogers
        quoteList.add("Don't let yesterday take up too much of today")
        //Vince Lombardi
        quoteList.add("It's not whether you get knocked down, it's whether you get up")
        //Johann Wolfgang Von Goethe
        quoteList.add("Knowing is not enough, we must apply. Wishing is not enough; we must do")


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

        inspireQuote = inspirationalQuote
        inspireQuote.text = quoteList.random()


    }
}
