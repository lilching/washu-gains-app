package com.example.washugains.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.welcome_page.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)
    }

    override fun onStart() {
        super.onStart()
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

    }
}
