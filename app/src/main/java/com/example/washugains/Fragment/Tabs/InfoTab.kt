package com.example.washugains.Fragment.Tabs

import EditInfoFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.washugains.Activity.LoginPage
//import com.example.washugains.Fragment.EditInfoFragment
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

        val fragment = EditInfoFragment()
        var bundle = Bundle()
        bundle.putString("username", username)
        bundle.putInt("feet",feet)
        bundle.putInt("inches",inches)
        bundle.putInt("weight", weight)
        bundle.putInt("calories", calories)
        fragment.arguments = bundle
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()

    }

}