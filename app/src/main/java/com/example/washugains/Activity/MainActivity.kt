package com.example.washugains.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.welcome_page.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)
    }

    override fun onStart() {
        super.onStart()

        //get user information (i.e. username) from Firebase or SignUpPage
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        db = FirebaseFirestore.getInstance()
        if (user?.uid != null) {
            //retrieve data from Firebase
            db.collection("users").document(user.uid).get()

                .addOnSuccessListener { document ->
                    if (document != null) {
                        val data = document.data
                        val username = data?.get("username") as String

                        //grabs text from welcome_page
                        if (username != null) {
                            usernameText.text = username
                        }
                    }
                }
        }

        //grabs element from welcome_page
        infoButton = welcomeInfo
        progressButton = welcomeProgress
        addButton = welcomeAdd

        infoButton.setOnClickListener {
            val intent = Intent(this, InfoPage::class.java)
            startActivity(intent)
        }

        progressButton.setOnClickListener {
            val intent = Intent(this, ProgressPage::class.java)
            startActivity(intent)
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddPage::class.java)
            startActivity(intent)
        }
    }
}
