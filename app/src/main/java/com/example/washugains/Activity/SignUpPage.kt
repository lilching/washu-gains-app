package com.example.washugains.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.DataClass.UserInformation
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.signup_page.*
import java.time.LocalDate

class SignUpPage : AppCompatActivity() {

    lateinit var signUpButton : Button
    lateinit var loginButton : Button
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)
        db = FirebaseFirestore.getInstance()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {

        super.onStart()


        //grab button from signup_page
        signUpButton = signUp
        signUpButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            val username = userSignUpInput.text.toString()
            val calorieGoal = 0
            val feet = 0
            val inches = 0
            val weight = 0

            if (emailSignUpInput.text.toString() == "" || passSignUpInput.text.toString() == "") {
                val toast=Toast.makeText(this,"Failed! Please fill in ALL inputs!",Toast.LENGTH_SHORT)
                toast.show()
            }
            else {
                mAuth.createUserWithEmailAndPassword(
                    emailSignUpInput.text.toString(),
                    passSignUpInput.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser

                            //update username
                            val profileUpdate = UserProfileChangeRequest.Builder()
                                .setDisplayName(username).build()
                            user?.updateProfile(profileUpdate)

                            //bundle information in created data class UserInformation
                            val userInfo =
                                UserInformation(
                                    username,
                                    calorieGoal,
                                    feet,
                                    inches,
                                    weight
                                )

                            val userMap: MutableMap<String, Any> = HashMap()
                            userMap["username"] = userInfo.username

                            if (user?.uid != null) {
                                db.collection("users").document(user?.uid)
                                    .set(userMap)

                                val dateMap =
                                    HashMap<String, DailyInfo>() as MutableMap<String, Any>
                                for (i in 0..30 as Long) {
                                    val date =
                                        LocalDate.now().minusDays(i).toString()
                                    dateMap.put(date, DailyInfo(date))
                                }

                                var batch = db.batch()
                                for (date in dateMap.keys) {
                                    var docRef = db.collection("users").document(user.uid)
                                        .collection("dates")
                                        .document(date)
                                    batch.set(
                                        docRef,
                                        dateMap.getOrDefault(date, DailyInfo(date))
                                    )
                                }
                                batch.commit()


                                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this, InputPage::class.java)
                                intent.putExtra("username", username)
                                startActivity(intent)
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }

        loginButton = loginBack
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}