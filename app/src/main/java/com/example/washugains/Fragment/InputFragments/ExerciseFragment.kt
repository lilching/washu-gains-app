package com.example.washugains.Fragment.InputFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.Adapter.ExerciseAdapter
import com.example.washugains.DataClass.Exercise
import com.example.washugains.R
import com.google.firebase.database.*
import com.sakebook.android.library.multilinedevider.MultiLineDivider
import kotlinx.android.synthetic.main.exercise_fragment.*

class ExerciseFragment : Fragment() {

    val exerciseList : ArrayList<Exercise> = ArrayList()
    var exerciseString : ArrayList<String> = ArrayList()

    val exerciseMETlist: ArrayList<Int> = ArrayList()

    private lateinit var exerciseSearch : androidx.appcompat.widget.SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.exercise_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        val recyclerView = view?.findViewById<RecyclerView>(R.id.exerciseRecyclerView)
        val adapter = ExerciseAdapter(exerciseString, exerciseMETlist)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val multiLineDivider = MultiLineDivider(context!!)
        recyclerView?.addItemDecoration(multiLineDivider)

        val ref = FirebaseDatabase.getInstance().getReference("sports")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                exerciseList.clear()
                for (productSnapshot in dataSnapshot.children) {
                    val exercise = productSnapshot.getValue(Exercise::class.java)
                    exerciseList.add(exercise!!)
                    if (exercise!=null){
                        exerciseString.add(exercise.activity)
                        exerciseMETlist.add(exercise.met)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException()
            }
        })

        //grabs searchView from exercise_fragment
        exerciseSearch = exercise_search
        exerciseSearch.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
//                adapter.filter.filter(query)
                return false
            }
        })
    }

    fun caloriesBurned(MET: Int, time: Int, weight: Int): Int{
        val calories = ((MET*weight/2.2)*time/60) as Int
        return calories
    }
}