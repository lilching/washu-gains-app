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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    //private var dailyInfo:LiveData<DailyInfo> = MutableLiveData()
   // private var dailyInfo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.progress_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter =
            ProgressPagerAdapter(
                this.childFragmentManager
            )



        progressViewPager.adapter = fragmentAdapter

        progressTabsMain.setupWithViewPager(progressViewPager)
    }

    override fun onStart() {
        super.onStart()
        progressViewPager.setCurrentItem(0)

    }


}
 class ProgressPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> StatsFragment()
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