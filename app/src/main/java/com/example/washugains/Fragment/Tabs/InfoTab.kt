package com.example.washugains.Fragment.Tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.washugains.Activity.LoginPage
import com.example.washugains.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.info_page.*

class InfoTab : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var updateButton : Button
    private lateinit var logoutButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.info_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        var feet = 0
        var inches = 0
        var weight = 0
        var calories = 0

        val username = arguments?.getString("username")
        infoUserText.text = username

        //grab element from info_page
        logoutButton = logout
        updateButton = myInfoInput

        db.collection("users").whereEqualTo("username", username).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        feet = document.get("feet").toString().toInt()
                        inches = document.get("inches").toString().toInt()
                        weight = document.get("weight").toString().toInt()
                        calories = document.get("calories").toString().toInt()
                    }
                }
            })

        logoutButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val intent = Intent(context, LoginPage::class.java)
            startActivity(intent)
        }

        updateButton.setOnClickListener{
            var feet = myFeetInput.text.toString()
            var inches = myInchInput.text.toString()
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

                Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_LONG).show()
            }
        }

    }

}