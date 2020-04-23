package com.example.washugains.Activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.Fragment.Tabs.AddTab
import com.example.washugains.Fragment.Tabs.InfoTab
import com.example.washugains.Fragment.Tabs.ProgressTab
import com.example.washugains.Fragment.Tabs.WelcomePage
import com.example.washugains.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    var username:String ="Buddy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        username = intent.getStringExtra("username")


        val fragment = WelcomePage()
        val bundle = Bundle()
        bundle.putString("username", username)
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerFrag, fragment)
        transaction.commit()

    }

    override fun onStart() {
        super.onStart()




        //grabs element from welcome_page
        bottomNav = bottomInfoBar
        bottomNav.menu.getItem(0).isCheckable = false
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabs_add -> {
                    item.isCheckable = true
                    val fragment = AddTab()
                    val bundle = Bundle()
                    bundle.putString("username", username)
                    fragment.arguments = bundle
                    val transactionNew = supportFragmentManager.beginTransaction()
                    transactionNew.replace(R.id.containerFrag, fragment)
                    transactionNew.commit()
                }
                R.id.tabs_progress -> {
                    item.isCheckable = true
                    val fragment = ProgressTab()
                    val bundle = Bundle()
                    bundle.putString("username", username)
                    fragment.arguments = bundle
                    val transactionNew = supportFragmentManager.beginTransaction()
                    transactionNew.replace(R.id.containerFrag, fragment)
                    transactionNew.commit()
                }
                R.id.tabs_person -> {
                    item.isCheckable = true
                    val fragment = InfoTab()
                    val bundle = Bundle()
                    bundle.putString("username", username)
                    fragment.arguments = bundle
                    val transactionNew = supportFragmentManager.beginTransaction()
                    transactionNew.replace(R.id.containerFrag, fragment)
                    transactionNew.commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }


}
