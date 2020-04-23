package com.example.washugains.Fragment.ProgressFragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.Adapter.ExerciseAddedAdapter
import com.example.washugains.DataClass.Exercise
import com.example.washugains.ExerciseAddedModel.ExerciseAddedViewModel
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.sakebook.android.library.multilinedevider.MultiLineDivider

class AddedExerciseFragment:Fragment() {
    val exerciseList : ArrayList<Exercise> = ArrayList()
    private lateinit var viewModel : ExerciseAddedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.added_exercise_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.exerciseAddedRecyclerView)
        viewModel= ViewModelProvider(this).get(ExerciseAddedViewModel::class.java)
        val adapter = ExerciseAddedAdapter(exerciseList,viewModel)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val multiLineDivider = MultiLineDivider(context!!)
        recyclerView?.addItemDecoration(multiLineDivider)


        val uid= FirebaseAuth.getInstance().uid
        if (uid!=null) {
            viewModel.updateExerciseItems(uid)
        }
        viewModel!!.getExerciseList().observe(this, Observer { exerciseItems ->
            exerciseList.clear()
            exerciseList.addAll(exerciseItems)
            adapter.notifyDataSetChanged()
        })
    }
}
