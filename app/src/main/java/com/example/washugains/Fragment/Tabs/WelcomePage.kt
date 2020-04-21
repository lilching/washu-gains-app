package com.example.washugains.Fragment.Tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.washugains.DataClass.Quote
import com.example.washugains.R
import kotlinx.android.synthetic.main.welcome_page.*

class WelcomePage : Fragment() {
    private lateinit var inspireQuote : TextView
    private lateinit var inspireAuthor : TextView

    private val waltDisney = Quote("\"The way to get started is to quit talking and begin doing.\"",
        "Walt Disney")
    private val winstonChurchill = Quote("\"The pessimist sees difficulty in every opportunity. The optimist sees the " +
            "opportunity in every difficulty.\"",
        "Winston Churchill")
    private val willRogers = Quote("\"Don't let yesterday take up too much of today.\"",
        "Will Rogers")
    private val vinceLombardi = Quote("\"It's not whether you get knocked down, it's whether you get up.\"",
        "Vince Lombardi")
    private val johannWolfgang = Quote("\"Knowing is not enough, we must apply. Wishing is not enough, we must do.\"",
        "Johann Wolfgang")


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

        val quoteList = ArrayList<Quote>()
        quoteList.add(waltDisney)
        quoteList.add(winstonChurchill)
        quoteList.add(willRogers)
        quoteList.add(vinceLombardi)
        quoteList.add(johannWolfgang)

        val randAuthor = quoteList.random()
        inspireQuote = inspirationalQuote
        inspireQuote.text = randAuthor.quote
        inspireAuthor = quoteAuthor
        inspireAuthor.text = "- " + randAuthor.author


    }
}
