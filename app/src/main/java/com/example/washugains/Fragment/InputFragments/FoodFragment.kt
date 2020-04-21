package com.example.washugains.Fragment.InputFragments


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.Adapter.FoodAdapter
import com.example.washugains.Adapter.MLAdapter
import com.example.washugains.DataClass.Food
import com.example.washugains.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.sakebook.android.library.multilinedevider.MultiLineDivider
import kotlinx.android.synthetic.main.food_fragment.*


class FoodFragment : Fragment() {

    private lateinit var foodSearch : SearchView

    val foodList : ArrayList<Food> = ArrayList()
    val foodString : ArrayList<String> = ArrayList()

    //for camera request code
    val REQUEST_CAMERA_PERMISSIONS = 1;
    val IMAGE_CAPTURE_CODE = 2;
    val IMAGE_FROM_PHOTOS = 3
    val REQUEST_EXTERNAL_PERMISSIONS = 4


    lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.food_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        val recyclerView = view?.findViewById<RecyclerView>(R.id.foodRecyclerView)
        val adapter = FoodAdapter(foodList, foodString)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val multiLineDivider = MultiLineDivider(context!!)
        recyclerView?.addItemDecoration(multiLineDivider)

        val ref = FirebaseDatabase.getInstance().getReference("food")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodList.clear()
                for (productSnapshot in dataSnapshot.children) {
                    val food = productSnapshot.getValue(Food::class.java)
                    if(food!=null) {
                        foodList.add(food)
                        foodString.add(food.name)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException()
            }
        })


        //grabs searchView from food_fragment
        foodSearch = food_search
        foodSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        food_camera.setOnClickListener {
            //code by https://medium.com/@hasangi/capture-image-or-choose-from-gallery-photos-implementation-for-android-a5ca59bc6883

            val options =
                arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Choose your profile picture")


            builder.setItems(options) { dialog, item ->
                when (options[item]) {
                    "Take Photo" -> {

                        var permission = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.CAMERA)

                        if(permission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(android.Manifest.permission.CAMERA),REQUEST_CAMERA_PERMISSIONS)
                        } else {
                            val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)

                        }

                    }
                    "Choose from Gallery" -> {
                        var permission = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                        if(permission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_EXTERNAL_PERMISSIONS)
                        } else {
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, IMAGE_FROM_PHOTOS)
                        }
                    }
                    else -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        if(requestCode == IMAGE_CAPTURE_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                val bitmap = data!!.extras!!["data"] as Bitmap

                val settingsDialog = Dialog(context!!, DialogFragment.STYLE_NO_FRAME)
                settingsDialog.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
                settingsDialog.setContentView(
                    layoutInflater.inflate(
                        R.layout.photo_display
                        , null
                    )
                )
                val imageView=settingsDialog.findViewById<ImageView>(R.id.photoTaken)
                imageView.setImageBitmap(bitmap)
                val dismissButton=settingsDialog.findViewById<Button>(R.id.dismissButton)


                settingsDialog.show()
                dismissButton.setOnClickListener {
                    settingsDialog.dismiss()

                }



                val fireImage = FirebaseVisionImage.fromBitmap(bitmap)

                val options = FirebaseVisionCloudImageLabelerOptions.Builder()
                              .setConfidenceThreshold(0.7f)
                              .build()
                val labeler = FirebaseVision.getInstance().getCloudImageLabeler(options)

                labeler.processImage(fireImage)
                    .addOnSuccessListener { labels ->
                        val recyclerView = settingsDialog.findViewById<RecyclerView>(R.id.resultRecyclerView)
                        val adapter = MLAdapter(labels)
                        recyclerView?.adapter = adapter
                        recyclerView?.layoutManager = LinearLayoutManager(context)
                        val multiLineDivider = MultiLineDivider(context!!)
                        recyclerView?.addItemDecoration(multiLineDivider)
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // todo
                    }


            }
        }


        if(requestCode == IMAGE_FROM_PHOTOS && resultCode==Activity.RESULT_OK){
            val selectedImage = data!!.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            if (selectedImage != null) {
                val cursor: Cursor? = activity?.contentResolver?.query(
                    selectedImage,
                    filePathColumn, null, null, null
                )
                if (cursor != null) {
                    cursor.moveToFirst()
                    val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath: String = cursor.getString(columnIndex)


                    val settingsDialog = Dialog(context!!, DialogFragment.STYLE_NO_FRAME)
                    settingsDialog.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
                    settingsDialog.setContentView(
                        layoutInflater.inflate(
                            R.layout.photo_display
                            , null
                        )
                    )
                    val imageView = settingsDialog.findViewById<ImageView>(R.id.photoTaken)
                    var bitmap: Bitmap?=BitmapFactory.decodeFile(picturePath)
                     if (bitmap==null){
                            val optionsBitmap = BitmapFactory.Options()
                            optionsBitmap.inSampleSize = 2
                            bitmap = BitmapFactory.decodeFile(picturePath, optionsBitmap)

                    }
                    if (bitmap!=null) {

                        imageView.setImageBitmap(bitmap)
                        cursor.close()
                        val dismissButton = settingsDialog.findViewById<Button>(R.id.dismissButton)


                        settingsDialog.show()
                        dismissButton.setOnClickListener {
                            settingsDialog.dismiss()

                        }


                        val fireImage = FirebaseVisionImage.fromBitmap(bitmap)

                        val options = FirebaseVisionCloudImageLabelerOptions.Builder()
                            .setConfidenceThreshold(0.7f)
                            .build()
                        val labeler = FirebaseVision.getInstance().getCloudImageLabeler(options)

                        labeler.processImage(fireImage)
                            .addOnSuccessListener { labels ->
                                val recyclerView =
                                    settingsDialog.findViewById<RecyclerView>(R.id.resultRecyclerView)
                                val adapter = MLAdapter(labels)
                                recyclerView?.adapter = adapter
                                recyclerView?.layoutManager = LinearLayoutManager(context)
                                val multiLineDivider = MultiLineDivider(context!!)
                                recyclerView?.addItemDecoration(multiLineDivider)
                            }
                            .addOnFailureListener { e ->
                                // Task failed with an exception
                                // todo
                            }
                    }
                    else{
                        //if bitmap is null
                        Toast.makeText(context,"Cannot open the file@",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }


}