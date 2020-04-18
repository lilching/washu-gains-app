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
import com.example.washugains.Activity.MainActivity
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate


class StatsFragment() : Fragment(){

    private val db= FirebaseFirestore.getInstance()
    private val uid= FirebaseAuth.getInstance().uid
    private var dailyData:DailyInfo?=null
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
        val pie: PieChart? = view?.findViewById(R.id.pieChart)
        if (uid != null) {
            db.collection("users").document(uid).collection("dates")
                .document(LocalDate.now().toString())
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    if (documentSnapshot != null) {
                        dailyData = documentSnapshot.toObject(DailyInfo::class.java)

                        if (pie != null && dailyData != null) {
                            pie.setUsePercentValues(false)
                            pie.description.isEnabled = false
                            val tf =
                                Typeface.createFromAsset(requireContext().assets, "OpenSans-Regular.ttf")
                            val tf_light =
                                Typeface.createFromAsset(requireContext().assets, "OpenSans-Light.ttf")



                            pie.setExtraOffsets(5f, 5f, 5f, 5f)
                            pie.dragDecelerationFrictionCoef = 0.95f
                            pie.isDrawHoleEnabled = true
                            pie.animateY(1400, Easing.EaseInOutQuad)


                            pie.transparentCircleRadius = 55f
                            val entries: MutableList<PieEntry> = ArrayList()
                            pie.setEntryLabelTextSize(15f)
                            pie.setEntryLabelColor(Color.DKGRAY)
                            pie.setEntryLabelTypeface(tf_light)


                            entries.add(PieEntry(roundTo2Helper(dailyData!!.sugars), "Sugar"))
                            entries.add(PieEntry(roundTo2Helper(dailyData!!.protein), "Protein"))
                            entries.add(PieEntry(roundTo2Helper(dailyData!!.fat), "Fat"))
                            entries.add(PieEntry(roundTo2Helper(dailyData!!.carb), "Carb"))
                            val set = PieDataSet(entries, "")
                            set.valueFormatter = GramValueFormatter()
                            set.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
                            set.sliceSpace = 4f
                            set.isHighlightEnabled = true

                            set.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE)

                            val data = PieData(set)

                            pie.centerText = "WHAT YOU ATE TODAY"
                            pie.setCenterTextTypeface(tf)
                            pie.setCenterTextSize(20f)
                            pie.setCenterTextColor(Color.DKGRAY)
                            data.setValueTextSize(15f)
                            data.setValueTypeface(tf_light)

                            data.setValueTextColor(Color.DKGRAY)
                            pie?.setData(data)
                            //setting the legend
                            val legend = pie.legend
                            legend.isEnabled = true
                            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                            legend.orientation = Legend.LegendOrientation.VERTICAL
                            legend.setDrawInside(true)
                            legend.formSize = 10f
                            legend.form = Legend.LegendForm.CIRCLE
                            legend.textSize = 15f
                            // set custom labels and colors
                            var total =
                                dailyData!!.sugars + dailyData!!.protein + dailyData!!.fat + dailyData!!.carb
                            if (total == 0.0) {
                                total = 1.0
                            }
                            legend.entries[0].label =
                                "Sugar:  " + roundTo2Helper(dailyData!!.sugars * 100 / total).toString() + "%"
                            legend.entries[1].label =
                                "Protein:  " + roundTo2Helper(dailyData!!.protein * 100 / total).toString() + "%"
                            legend.entries[2].label =
                                "Fat:  " + roundTo2Helper(dailyData!!.fat * 100 / total).toString() + "%"
                            legend.entries[3].label =
                                "Carb:  " + roundTo2Helper(dailyData!!.carb * 100 / total).toString() + "%"

                            pie.invalidate()
                        }


                    }
                }
        }
    }


    class GramValueFormatter: ValueFormatter(){

        override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
            return pieEntry?.value.toString()+"g"
        }



    }


    fun roundTo2Helper(num:Double):Float{
        return Math.round(num * 100) /100f
    }

}