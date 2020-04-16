package com.example.washugains.Fragment.Tabs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.Fragment.ProgressFragments.AddedExerciseFragment
import com.example.washugains.Fragment.ProgressFragments.AddedFoodFragment
import com.example.washugains.Fragment.ProgressFragments.StatsFragment
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.progress_page.*
import java.time.LocalDate

class ProgressTab : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var dateMap:HashMap<String,DailyInfo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.progress_page, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter =
            ProgressPagerAdapter(
                activity!!.supportFragmentManager
            )


        progressViewPager.adapter = fragmentAdapter

        progressTabsMain.setupWithViewPager(progressViewPager)

        db= FirebaseFirestore.getInstance()

        //getting the data of the past month
        dateMap= HashMap()
        for (i in 0..30 as Long) {
            val date =
                LocalDate.now().minusDays(i).toString()
            dateMap.put(date, DailyInfo(date))
        }
        val mAuth = FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        if(user!=null) {
            db.collection("users").document(user.uid).collection("dates").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (dateMap.get(document.id) != null) {
                            dateMap.put(document.id, document.toObject(DailyInfo::class.java))
                        }
                    }
                }
            db.collection("users").document(user.uid).collection("dates").document(LocalDate.now().toString()).addSnapshotListener{
                    document, e ->
                if (document != null && document.exists()) {
                    document.toObject(DailyInfo::class.java)?.let { dateMap.put(document.id, it) }
                }
            }
        }

    }

    inner class ProgressPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    StatsFragment(dateMap)
                }
                1 -> AddedFoodFragment()
                else -> AddedExerciseFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Daily Stats"
                1 -> "Food"
                else -> "Exercise"
            }
        }

    }

}