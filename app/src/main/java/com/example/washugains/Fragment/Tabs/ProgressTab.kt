package com.example.washugains.Fragment.Tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.washugains.Fragment.ProgressFragments.AddedExerciseFragment
import com.example.washugains.Fragment.ProgressFragments.AddedFoodFragment
import com.example.washugains.Fragment.ProgressFragments.MonthlyFragment
import com.example.washugains.Fragment.ProgressFragments.StatsFragment
import com.example.washugains.R
import kotlinx.android.synthetic.main.progress_page.*

class ProgressTab : Fragment() {

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
        progressViewPager.offscreenPageLimit=0

    }


}
 class ProgressPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> StatsFragment()
            1 -> MonthlyFragment()
            2 -> AddedFoodFragment()
            else -> AddedExerciseFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Daily Stats"
            1 -> "Monthly Progress"
            2->  "Food"
            else -> "Exercise"
        }
    }

}