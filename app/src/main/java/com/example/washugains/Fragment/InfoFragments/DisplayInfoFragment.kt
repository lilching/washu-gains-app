package com.example.washugains.Fragment.InfoFragments

import EditInfoFragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.washugains.Activity.LoginPage

import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.display_info_fragment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DisplayInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplayInfoFragment : Fragment() {
    private lateinit var updateButton : Button
    private lateinit var logoutButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.display_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var feet = arguments!!.getInt("feet")
        var inches = arguments!!.getInt("inches")
        var weight = arguments!!.getInt("weight")
        var calorieGoal = arguments!!.getInt("calorieGoal")
        var username = arguments!!.getString("username")

        myFeet.text = "Height:   " + feet.toString() + "' "
        myInch.text = inches.toString() + "''"
        myWeight.text = "Weight: " + weight.toString() + " lbs"
        myCalories.text = "Calorie Goal: " + calorieGoal.toString()

        logoutButton = logout
        updateButton = myInfoEdit

        logoutButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val intent = Intent(context, LoginPage::class.java)
            startActivity(intent)
        }

        updateButton.setOnClickListener{
            val fragment = EditInfoFragment()
            var bundle = Bundle()
            bundle.putString("username", username)
            bundle.putInt("feet",feet)
            bundle.putInt("inches",inches)
            bundle.putInt("weight", weight)
            bundle.putInt("calorieGoal", calorieGoal)
            fragment.arguments = bundle
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }


    }
}

