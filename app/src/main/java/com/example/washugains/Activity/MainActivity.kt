package com.example.washugains.Activity


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.washugains.Fragment.InputFragments.FoodFragment
import com.example.washugains.Fragment.Tabs.AddTab
import com.example.washugains.Fragment.Tabs.InfoTab
import com.example.washugains.Fragment.Tabs.ProgressTab
import com.example.washugains.Fragment.Tabs.WelcomePage
import com.example.washugains.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    //  private lateinit var db : FirebaseFirestore
    //for camera request code
    val REQUEST_CAMERA_PERMISSIONS = 1;
    val IMAGE_CAPTURE_CODE = 2;
    var username:String ="Buddy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        username = intent.getStringExtra("username")


        val fragment = WelcomePage()
        val bundle = Bundle()
        bundle.putString("username", username)
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerFrag, fragment)
        transaction.commit()

    }

    override fun onStart() {
        super.onStart()




        //grabs element from welcome_page
        bottomNav = bottomInfoBar
        bottomNav.menu.getItem(0).isCheckable = false
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabs_add -> {
                    item.isCheckable = true
                    val fragment = AddTab()
                    val bundle = Bundle()
                    bundle.putString("username", username)
                    fragment.arguments = bundle
                    val transactionNew = supportFragmentManager.beginTransaction()
                    transactionNew.replace(R.id.containerFrag, fragment)
                    transactionNew.commit()
                }
                R.id.tabs_progress -> {
                    item.isCheckable = true
                    val fragment = ProgressTab()
                    val bundle = Bundle()
                    bundle.putString("username", username)
                    fragment.arguments = bundle
                    val transactionNew = supportFragmentManager.beginTransaction()
                    transactionNew.replace(R.id.containerFrag, fragment)
                    transactionNew.commit()
                }
                R.id.tabs_person -> {
                    item.isCheckable = true
                    val fragment = InfoTab()
                    val bundle = Bundle()
                    bundle.putString("username", username)
                    fragment.arguments = bundle
                    val transactionNew = supportFragmentManager.beginTransaction()
                    transactionNew.replace(R.id.containerFrag, fragment)
                    transactionNew.commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }


}
