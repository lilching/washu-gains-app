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
import com.sakebook.android.library.multilinedevider.MultiLineDivider

class ExerciseFragment : Fragment() {

    val exerciseList : ArrayList<Exercise> = ArrayList()

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
        val adapter = ExerciseAdapter(exerciseList)
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
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException()
            }
        })


    }
}