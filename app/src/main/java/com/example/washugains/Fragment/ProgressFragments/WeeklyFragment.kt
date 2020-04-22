package com.example.washugains.Fragment.ProgressFragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.*


class WeeklyFragment:Fragment() {

    lateinit var tf: Typeface
    lateinit var tf_light: Typeface
    val dateMap = HashMap<String, DailyInfo>()
    var dailyGoal = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tf =
            Typeface.createFromAsset(activity?.assets, "OpenSans-Regular.ttf")
        tf_light =
            Typeface.createFromAsset(activity?.assets, "OpenSans-Light.ttf")
        return inflater.inflate(R.layout.barchart_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val chart:BarChart? = view?.findViewById(R.id.barchart)
        if (user?.uid != null) {
            //retrieve data from Firebase
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val data = document.data
                        //   val username = data?.get("username") as String
                        dailyGoal= Integer.parseInt(data?.get("calorieGoal") as String)

                        //retrieve the data of previous 7 days
                        for (i in 0..6 as Long) {
                            val date =
                                LocalDate.now().minusDays(i).toString()
                            dateMap[date] = DailyInfo(date)
                        }
                        db.collection("users").document(user.uid)
                            .collection("dates").get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    if (dateMap[document.id] != null) {
                                        dateMap[document.id] = document.toObject(
                                            DailyInfo::class.java
                                        )
                                    }
                                }

                                //drawing the chart

                                if(chart!=null) {
                                    chart.getDescription().setEnabled(false)

                                    chart.setPinchZoom(false)

                                    chart.setDrawBarShadow(false)

                                    chart.setDrawGridBackground(false)


                                    val l: Legend = chart.getLegend()
                                    l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                                    l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                                    l.orientation = Legend.LegendOrientation.VERTICAL
                                    l.setDrawInside(true)
                                    l.typeface = tf_light
                                    l.yOffset = 20f
                                    l.xOffset = 10f
                                    l.yEntrySpace = 0f
                                    l.textSize = 8f


                                    val leftAxis: YAxis = chart.getAxisLeft()
                                    leftAxis.typeface = tf_light
                                    leftAxis.valueFormatter = LargeValueFormatter()
                                    leftAxis.setDrawGridLines(false)
                                    leftAxis.spaceTop = 35f
                                    leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


                                    chart.getAxisRight().setEnabled(false)

                                    val valuesCalBurned = ArrayList<BarEntry>()
                                    val valuesCalEaten = ArrayList<BarEntry>()
                                    val valuesGoal = ArrayList<BarEntry>()



                                    val daysInAWeek = ArrayList<String>()
                                    for (i  in 0..6 as Long) {
                                        val date = LocalDate.now().minusDays(i).toString()
                                        daysInAWeek.add(date)
                                        val thisday=dateMap[date]
                                        if (thisday!=null) {
                                            valuesCalBurned.add(BarEntry(i.toFloat(), thisday.caloriesBurned.toFloat()))
                                            valuesCalEaten.add(BarEntry(i.toFloat(), thisday.calories.toFloat()))
                                            valuesGoal.add(BarEntry(i.toFloat(),dailyGoal.toFloat()))

                                        }
                                    }

                                    val set1: BarDataSet
                                    val set2: BarDataSet
                                    val set3: BarDataSet


                                    if (chart.data != null && chart.data.dataSetCount > 0) {
                                        set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                                        set2 = chart.data.getDataSetByIndex(1) as BarDataSet
                                        set3 = chart.data.getDataSetByIndex(2) as BarDataSet

                                        set1.values = valuesGoal
                                        set2.values = valuesCalEaten
                                        set3.values = valuesCalBurned

                                        chart.data.notifyDataChanged()
                                        chart.notifyDataSetChanged()
                                    } else {
                                        // create 4 DataSets
                                        set1 = BarDataSet(valuesCalBurned, "Goal")
                                        set1.color = Color.rgb(104, 241, 175)
                                        set2 = BarDataSet(valuesCalEaten, "Calories Eaten")
                                        set2.color = Color.rgb(164, 228, 251)
                                        set3 = BarDataSet(valuesGoal, "Calories Burned")
                                        set3.color = Color.rgb(242, 247, 158)

                                        val data = BarData(set1, set2, set3)
                                        data.setValueFormatter(LargeValueFormatter())
                                        data.setValueTypeface(tf_light)
                                        chart.data = data
                                    }


                                    chart.barData.barWidth = 0.2f


                                    // restrict the x-axis range

//            val xAxis: XAxis = chart.xAxis
//            xAxis.typeface = tf_light
//            xAxis.granularity = 1f
//            xAxis.setCenterAxisLabels(true)
//            xAxis.valueFormatter = object : ValueFormatter() {
//                override fun getFormattedValue(value: Float): String {
//                    return value.toString()
//                }
//                override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
//                    return daysInAWeek[value.toInt()]
//                }
//
//            }
//            xAxis.granularity = 1f // only intervals of 1 day
//





                                    // add a nice and smooth animation
                                    chart.animateY(1400, Easing.EaseInOutQuad)
                                    chart.invalidate()


                                }


                            }

                    }
                }
        }






    }


}