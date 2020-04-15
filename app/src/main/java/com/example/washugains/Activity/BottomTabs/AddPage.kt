package com.example.washugains.Activity.BottomTabs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.Fragment.InputFragments.ExerciseFragment
import com.example.washugains.Fragment.InputFragments.FoodFragment
import com.example.washugains.R
import kotlinx.android.synthetic.main.activity_main.*


class AddPage : AppCompatActivity() {

    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button
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