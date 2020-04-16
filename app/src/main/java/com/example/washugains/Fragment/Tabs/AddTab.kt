package com.example.washugains.Fragment.Tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.washugains.Fragment.InputFragments.ExerciseFragment
import com.example.washugains.Fragment.InputFragments.FoodFragment
import com.example.washugains.R
import kotlinx.android.synthetic.main.add_page.*

class AddTab : Fragment() {

    private lateinit var foodButton : Button
    private lateinit var exerciseButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment =
            FoodFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()


        foodButton = button21
        foodButton.setOnClickListener {
            val fragment =
                FoodFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
        exerciseButton = button22
        exerciseButton.setOnClickListener {
            val fragment =
                ExerciseFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }

}