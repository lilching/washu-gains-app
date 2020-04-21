package com.example.washugains.Fragment.ProgressFragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.washugains.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*


class MonthlyFragment:Fragment() {
   // val foodList : ArrayList<Food> = ArrayList()
    //todo viewModel
  //  private lateinit var viewModel : FoodAddedViewModel

    lateinit var tf: Typeface
    lateinit var tf_light: Typeface

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

    override fun onStart() {
        super.onStart()
        //sample code
        //todo retrieve data and plot it

        val chart:BarChart? = view?.findViewById(R.id.barchart)
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

            val xAxis: XAxis = chart.getXAxis()
            xAxis.typeface = tf_light
            xAxis.granularity = 1f
            xAxis.setCenterAxisLabels(true)
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toString()
                }
            }

            val leftAxis: YAxis = chart.getAxisLeft()
            leftAxis.typeface = tf_light
            leftAxis.valueFormatter = LargeValueFormatter()
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 35f
            leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


            chart.getAxisRight().setEnabled(false)

            val values1 = ArrayList<BarEntry>()
            val values2 = ArrayList<BarEntry>()
            val values3 = ArrayList<BarEntry>()
            val values4 = ArrayList<BarEntry>()

            val randomMultiplier: Float = 1000f
            // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"
            val groupCount: Int = 3
            val startYear = 1980
            val endYear = startYear + groupCount

            for (i  in startYear until endYear ) {
                values1.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
                values2.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
                values3.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
                values4.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
            }

            val set1: BarDataSet
            val set2: BarDataSet
            val set3: BarDataSet
            val set4: BarDataSet

            if (chart.data != null && chart.data.dataSetCount > 0) {
                set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                set2 = chart.data.getDataSetByIndex(1) as BarDataSet
                set3 = chart.data.getDataSetByIndex(2) as BarDataSet
                set4 = chart.data.getDataSetByIndex(3) as BarDataSet
                set1.values = values1
                set2.values = values2
                set3.values = values3
                set4.values = values4
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
            } else {
                // create 4 DataSets
                set1 = BarDataSet(values1, "Company A")
                set1.color = Color.rgb(104, 241, 175)
                set2 = BarDataSet(values2, "Company B")
                set2.color = Color.rgb(164, 228, 251)
                set3 = BarDataSet(values3, "Company C")
                set3.color = Color.rgb(242, 247, 158)
                set4 = BarDataSet(values4, "Company D")
                set4.color = Color.rgb(255, 102, 0)
                val data = BarData(set1, set2, set3, set4)
                data.setValueFormatter(LargeValueFormatter())
                data.setValueTypeface(tf_light)
                chart.data = data
            }

            val groupSpace = 0.08f
            val barSpace = 0.03f // x4 DataSet

            // specify the width each bar should have

            // specify the width each bar should have
            chart.barData.barWidth = 0.2f

            // restrict the x-axis range

            // restrict the x-axis range
            chart.xAxis.axisMinimum = startYear.toFloat()

            // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters

            // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
            chart.xAxis.axisMaximum =
                startYear + chart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
            chart.groupBars(startYear.toFloat(), groupSpace, barSpace)
            // add a nice and smooth animation

            // add a nice and smooth animation
            chart.animateY(1400, Easing.EaseInOutQuad)
            chart.invalidate()


        }





    }


}