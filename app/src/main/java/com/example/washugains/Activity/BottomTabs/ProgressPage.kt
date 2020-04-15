package com.example.washugains.Activity.BottomTabs

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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

class ProgressPage : AppCompatActivity() {

    private lateinit var infoButton : Button
    private lateinit var progressButton : Button
    private lateinit var addButton : Button
    private lateinit var db : FirebaseFirestore
    private lateinit var dateMap:HashMap<String,DailyInfo>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   val dailyInfoList=intent.getParcelableArrayListExtra<DailyInfo>("dailyInfoList")
        setContentView(R.layout.progress_page)


        val fragmentAdapter =
            ProgressPagerAdapter(
                supportFragmentManager
            )


        progressViewPager.adapter = fragmentAdapter

        progressTabsMain.setupWithViewPager(progressViewPager)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
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

        //grabs element from progress_page
        infoButton = progressInfo
        progressButton = progressProgress
        addButton = progressAdd

        infoButton.setOnClickListener {
            val intent = Intent(this, InfoPage::class.java)
            startActivity(intent)
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddPage::class.java)
            startActivity(intent)
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
