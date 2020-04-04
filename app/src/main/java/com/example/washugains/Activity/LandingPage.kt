package com.example.washugains.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.washugains.Fragment.LoginPage
import com.example.washugains.R
import com.example.washugains.Fragment.SignUpPage
import kotlinx.android.synthetic.main.landing_page.*


class LandingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_page)

        val pageNumb = intent.getIntExtra("pageNumb", 0)
        val fragmentAdapter =
            MyPagerAdapter(
                supportFragmentManager
            )

        //grabs viewPage element from landing_page
        viewPager.adapter = fragmentAdapter
        if (pageNumb != null) {
            viewPager.currentItem = pageNumb
        }

        //grabs tabViewer element from landing_page
        tabsMain.setupWithViewPager(viewPager)
    }

    //creates tab view that is able to transition between login and sign up fragments
    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    LoginPage()
                }
                else -> SignUpPage()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Login"
                else -> "Sign-Up"
            }
        }

    }
}