package com.example.washugains.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.input_page.*

class InputPage: AppCompatActivity() {

    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_page)
    }


    override fun onStart() {
        super.onStart()
        db = FirebaseFirestore.getInstance()
        val username = intent.getStringExtra("username")
        var calories = intent?.getStringExtra("calories")
        val dailyInfoList=intent.getParcelableArrayListExtra<DailyInfo>("dailyInfoList")


        continueButton.setOnClickListener{
            var inches = inchInput.text.toString()
            var feet = feetInput.text.toString()
            var weight = weightInput.text.toString()
            calories = caloriesInput.text.toString()

            db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val reference = db.collection("users").document(document.id)
                            reference.update("calories", calories).addOnSuccessListener {
                                println("calories updated")
                            }
                            reference.update("feet", feet).addOnSuccessListener {
                                println("feet updated")
                            }
                            reference.update("inches", inches).addOnSuccessListener {
                                println("inches updated")
                            }
                            reference.update("weight", weight).addOnSuccessListener {
                                println("weight updated")
                            }
                        }
                    }
                })

            if (feet != "" && inches != "" && weight != "" && calories != "") {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("calories", calories)
                intent.putExtra("dailyInfoList",dailyInfoList)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
