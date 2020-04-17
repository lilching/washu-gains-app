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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.welcome_page.*



class WelcomePage : AppCompatActivity() {



  //  private lateinit var db : FirebaseFirestore
    private lateinit var bottomNav : BottomNavigationView
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
//        val calories=intent?.getStringExtra("calories")
        if (username != null) {
          usernameText.text = username
        }
        //grabs element from welcome_page
        bottomNav = bottomInfoBar
        bottomNav.selectedItemId = R.id.tabs_add
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabs_add -> {
                    val intent = Intent(this, AddPage::class.java)
                    startActivity(intent)
                }
                R.id.tabs_progress -> {
                    val intent = Intent(this, ProgressPage::class.java)
                    startActivity(intent)
                }
                R.id.tabs_person -> {
                    val intent = Intent(this, InfoPage::class.java)
                    startActivity(intent)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        inspireQuote = inspirationalQuote
        inspireQuote.text = quoteList.random()


    }
}
