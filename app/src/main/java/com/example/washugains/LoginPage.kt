package com.example.washugains

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.login_page.*

class LoginPage : Fragment() {

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
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(context, "Authentication Successful", Toast.LENGTH_SHORT)
                            .show()
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