package com.example.washugains.Fragment.ProgressFragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate


class StatsFragment(val dateMap: HashMap<String,DailyInfo>) : Fragment(){

    private val db= FirebaseFirestore.getInstance()
    private val uid= FirebaseAuth.getInstance().uid
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.chart_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val pie: PieChart?=view?.findViewById(R.id.pieChart)
        if(uid!=null) {
            db.collection("users").document(uid).collection("dates")
                .document(LocalDate.now().toString()).get().addOnSuccessListener { documentSnapshot ->
                    var dailyData = documentSnapshot.toObject(DailyInfo::class.java)

                    if (pie != null&&dailyData!=null) {
                        pie.setUsePercentValues(false)
                        pie.description.isEnabled = false
                        pie.setExtraOffsets(5f, 5f, 5f, 5f)
                        pie.dragDecelerationFrictionCoef = 0.95f
                        pie.isDrawHoleEnabled = true

                      //  pie.setHoleColor(Color.WHITE)
                       // pie.setBackgroundColor(Color.DKGRAY)
                        pie.transparentCircleRadius = 55f
                        val entries: MutableList<PieEntry> = ArrayList()
                        pie.setEntryLabelTextSize(20f)
                        pie.setEntryLabelColor(Color.DKGRAY)
                      //  val total=dailyData.sugars+dailyData.protein+dailyData.fat+dailyData.carb
                        entries.add(PieEntry(roundTo2Helper(dailyData.sugars), "Sugar"))
                        entries.add(PieEntry(roundTo2Helper(dailyData.protein), "Protein"))
                        entries.add(PieEntry(roundTo2Helper(dailyData.fat), "Fat"))
                        entries.add(PieEntry(roundTo2Helper(dailyData.carb), "Carb"))
                        val set = PieDataSet(entries, "What you ate")
                        set.colors = ColorTemplate.LIBERTY_COLORS.toList()
                        set.sliceSpace=4f

                        val data = PieData(set)
                        pie.centerText="What you ate today (grams)"
                        pie.setCenterTextSize(20f)
                        pie.setCenterTextColor(Color.DKGRAY)
                        data.setValueTextSize(20f)

                        data.setValueTextColor(Color.DKGRAY)
                        pie?.setData(data)
                        pie.invalidate()
                    }
                }
        }

    }

    fun roundTo2Helper( num:Double):Float{
        val ans=Math.round(num*100)/100f
        return ans
    }

}