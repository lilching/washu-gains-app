package com.example.washugains.Fragment.ProgressFragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import com.anychart.AnyChart
//import com.anychart.AnyChartView
//import com.anychart.chart.common.dataentry.DataEntry
//import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.R


import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class StatsFragment(val dateMap: HashMap<String,DailyInfo>) : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.chart_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val pie: PieChart?=view?.findViewById(R.id.pieChart)
        if (pie!=null) {

            pie.setUsePercentValues(true)
            pie.description.isEnabled=false
            pie.setExtraOffsets(5f,10f,5f,5f)
            pie.dragDecelerationFrictionCoef=0.95f
            pie.isDrawHoleEnabled=true
            pie.setHoleColor(Color.WHITE)
            pie.transparentCircleRadius=55f
            val entries: MutableList<PieEntry> = ArrayList()
            entries.add(PieEntry(18.5f, "Green"))
            entries.add(PieEntry(26.7f, "Yellow"))
            entries.add(PieEntry(24.0f, "Red"))
            entries.add(PieEntry(30.8f, "Blue"))
            val set = PieDataSet(entries, "What you ate")
             set.colors=ColorTemplate.VORDIPLOM_COLORS.toList()

            val data = PieData(set)
            data.setValueTextSize(10f)
            data.setValueTextColor(Color.YELLOW)
            pie?.setData(data)
            pie.invalidate()
        }
//        val pie = AnyChart.pie()
//
//        val data: MutableList<DataEntry> = ArrayList()
//        data.add(ValueDataEntry("John", 10000))
//        data.add(ValueDataEntry("Jake", 12000))
//        data.add(ValueDataEntry("Peter", 18000))
//
//        pie.data(data)
//
//        val anyChartView = view?.findViewById(R.id.pieChart) as AnyChartView
//        anyChartView.setChart(pie)
    }

}