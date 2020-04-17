package com.example.washugains.Fragment.Tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.washugains.R
import kotlinx.android.synthetic.main.welcome_page.*

class WelcomePage : Fragment() {
    private lateinit var inspireQuote : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  val dailyInfoList=intent.getParcelableArrayListExtra<DailyInfo>("dailyInfoList")

        val username = arguments?.getString("username")
        usernameText.text = username

        var quoteList = ArrayList<String>()
        //Walt Disney
        quoteList.add("The way to get started is to quit talking and begin doing")
        //Winston Churchill
        quoteList.add("The pessimist sees difficulty in every opportunity. The optimist sees the " +
                "opportunity in every difficulty")
        //Will Rogers
        quoteList.add("Don't let yesterday take up too much of today")
        //Vince Lombardi
        quoteList.add("It's not whether you get knocked down, it's whether you get up")
        //Johann Wolfgang Von Goethe
        quoteList.add("Knowing is not enough, we must apply. Wishing is not enough; we must do")

        inspireQuote = inspirationalQuote
        inspireQuote.text = quoteList.random()


    }
}
