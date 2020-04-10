package com.example.washugains.Fragment.UserAccess

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.washugains.Activity.InputPage
import com.example.washugains.Activity.WelcomePage
import com.example.washugains.R
import com.example.washugains.DataClass.UserInformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.signup_page.*

class SignUpPage : Fragment() {

    lateinit var signUpButton : Button
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_page, container, false)
    }

    override fun onStart() {

        super.onStart()
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings

        //grab button from signup_page
        signUpButton = signUp
        signUpButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            val username = userSignUpInput.text.toString()
            val calories = 0
            mAuth.createUserWithEmailAndPassword(
                emailSignUpInput.text.toString(),
                passSignUpInput.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail: success")
                        val user = mAuth.currentUser

                        //update username
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(username).build()
                        user?.updateProfile(profileUpdate)

                        //bundle information in created data class UserInformation
                        val userInfo =
                            UserInformation(
                                username,
                                calories
                            )

                        val userMap: MutableMap<String, Any> = HashMap()
                        userMap["username"] = userInfo.username

                        if (user?.uid != null) {
                            db.collection("users").document(user?.uid)
                                .set(userMap)
                        }

                        Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(context, InputPage::class.java)
                        intent.putExtra("username", username)
                        intent.putExtra("calories", calories)
                        startActivity(intent)
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}


