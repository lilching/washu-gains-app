package com.example.washugains.Fragment.InputFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.Adapter.ExerciseAdapter
import com.example.washugains.DataClass.Exercise
import com.example.washugains.R
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.sakebook.android.library.multilinedevider.MultiLineDivider
import kotlinx.android.synthetic.main.exercise_fragment.*

class ExerciseFragment : Fragment() {

    private lateinit var db : FirebaseFirestore

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

        db = FirebaseFirestore.getInstance()
        var username = arguments!!.getString("username")!!

        val recyclerView = view?.findViewById<RecyclerView>(R.id.exerciseRecyclerView)
        val adapter = ExerciseAdapter(exerciseString, exerciseList, exerciseMETlist, username)
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
                    if (exercise!=null){
                        exerciseString.add(exercise.activity)
                        exerciseList.add(exercise)
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

}