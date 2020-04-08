package com.example.washugains.Fragment.UserAccess

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.washugains.Activity.WelcomePage
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.welcome_page.*

class LoginPage : Fragment() {

    lateinit var  backdoorButton : Button
    lateinit var loginButton : Button
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.login_page, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        db = FirebaseFirestore.getInstance()

        //TODO remove backdoor when finished
        backdoorButton = backDoor
        backdoorButton.setOnClickListener {
            val intent = Intent(context, WelcomePage::class.java)
            startActivity(intent)
        }

        //grabs button from login_page
        loginButton = login
        loginButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()

            //grab inputted email and password from login_page
            mAuth.signInWithEmailAndPassword(
                emailLoginInput.text.toString(),
                passLoginInput.text.toString()
            )
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        db = FirebaseFirestore.getInstance()
                        if (user?.uid != null) {
                            //retrieve data from Firebase
                            db.collection("users").document(user.uid).get()

                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val data = document.data
                                        val username = data?.get("username") as String
                                        val intent = Intent(context, WelcomePage::class.java)
                                        intent.putExtra("username",username)
                                        startActivity(intent)
                                        Toast.makeText(context, "Authentication Successful", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(context, "Authentication Failure."+task.exception, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

        }

    }

}