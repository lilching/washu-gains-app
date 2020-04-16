package com.example.washugains.Activity.BottomTabs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.Fragment.InputFragments.ExerciseFragment
import com.example.washugains.Fragment.InputFragments.FoodFragment
import com.example.washugains.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firestore.admin.v1beta1.Progress
import kotlinx.android.synthetic.main.activity_main.*


class AddPage : AppCompatActivity() {

    private lateinit var bottomNav : BottomNavigationView
    private lateinit var foodButton : Button
    private lateinit var exerciseButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment =
            FoodFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()

    }

    override fun onStart() {
        super.onStart()
        //grabs element from activity_main
        bottomNav = bottomInfoBar
        bottomNav.selectedItemId = R.id.tabs_add
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
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

        foodButton = button21
        foodButton.setOnClickListener {
            val fragment =
                FoodFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
        exerciseButton = button22
        exerciseButton.setOnClickListener {
            val fragment =
                ExerciseFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }

}