package com.example.washugains.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DailyInfo (val date: String = "",
                      val calories:Int = 0,
                      val carb: Double = 0.0,
                      val fat: Double = 0.0,
                      val sugars: Double = 0.0,
                      val protein:Double = 0.0):Parcelable