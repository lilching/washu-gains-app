package com.example.washugains.Fragment.UserAccess

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.washugains.Activity.MainActivity
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.login_page.*
import java.time.LocalDate

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        db = FirebaseFirestore.getInstance()

        //TODO remove backdoor when finished
        backdoorButton = backDoor
        backdoorButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
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
                                    if (document.exists()) {
                                        val data = document.data
                                        val username = data?.get("username") as String
                                        var calories = data?.get("calories") as String

                                        //checking if the data for previous 30 days exists
                                        val dateMap= HashMap<String,DailyInfo>() as MutableMap<String,Any>
                                        for (i in 0..30 as Long) {
                                            val date =
                                                LocalDate.now().minusDays(i).toString()
                                            dateMap.put(date, DailyInfo(date))
                                        }
                                        db.collection("users").document(user.uid).collection("dates").get()
                                            .addOnSuccessListener {documents->
                                                for (document in documents){
                                                    if(dateMap.get(document.id)!=null){
                                                        dateMap.put(document.id,document.toObject(DailyInfo::class.java))
                                                    }
                                                }
                                                var batch = db.batch()
                                                for (date in dateMap.keys){
                                                    var docRef= db.collection("users").document(user.uid).collection("dates")
                                                        .document(date)
                                                    batch.set(docRef,dateMap.getOrDefault(date,DailyInfo(date)))
                                                }
                                                batch.commit()
                                            }



//                                        val calories = 0
                                        val intent = Intent(context, MainActivity::class.java)
                                        intent.putExtra("username",username)
                                        intent.putExtra("calories", calories)
                                      //  val dailyInfoList=ArrayList<DailyInfo>(dateMap.values as MutableCollection<out DailyInfo>)
                                      //  intent.putExtra("dailyInfoList",dailyInfoList)
                                        startActivity(intent)
                                        Toast.makeText(context, "Authentication Successful", Toast.LENGTH_SHORT).show()
                                       // Toast.makeText(context, currentDate, Toast.LENGTH_SHORT).show()
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