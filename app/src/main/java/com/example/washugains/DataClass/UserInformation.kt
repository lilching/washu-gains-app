package com.example.washugains.DataClass


//information to be associated with each account
data class UserInformation(
    val username : String,
    val calories: Int,
    val calorieGoal: Int,
    val feet: Int,
    val inches: Int,
    val weight: Int
)