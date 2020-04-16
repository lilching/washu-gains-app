package com.example.washugains.Activity.BottomTabs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.Activity.LandingPage
import com.example.washugains.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.info_page.*


class InfoPage : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore

    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button
    private lateinit var updateButton : Button
    private lateinit var logoutButton : Button
    private lateinit var bottomBar : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_page)
    }

    override fun onStart() {
        super.onStart()
        db = FirebaseFirestore.getInstance()
        val username = intent.getStringExtra("username")

        infoUserText.text = username

        //grab element from info_page
        bottomBar = bottomInfoBar
        logoutButton = logout
        updateButton = myInfoInput

        bottomInfoBar.selectedItemId = R.id.tabs_person
        bottomInfoBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabs_progress -> {
                    val intent = Intent(this, ProgressPage::class.java)
                    startActivity(intent)
                }
                R.id.tabs_add -> {
                    val intent = Intent(this, AddPage::class.java)
                    startActivity(intent)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }


        logoutButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

        updateButton.setOnClickListener{
            var height = myHeightInput.text.toString()
            var weight = myWeightInput.text.toString()
            var calories = myCaloriesInput.text.toString()

            db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val reference = db.collection("users").document(document.id)
                            reference.update("calories", calories).addOnSuccessListener {
                                println("calories updated")
                            }
                            reference.update("height", height).addOnSuccessListener {
                                println("height updated")
                            }
                            reference.update("weight", weight).addOnSuccessListener {
                                println("weight updated")
                            }
                        }
                    }
                })

            if (height != "" && weight != "" && calories != "") {

                myHeightInput.text.clear()
                myWeightInput.text.clear()
                myCaloriesInput.text.clear()

                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show()
            }
        }

    }
}