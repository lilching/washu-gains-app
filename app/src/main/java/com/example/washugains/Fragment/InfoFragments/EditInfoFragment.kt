
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.washugains.Activity.LoginPage
import com.example.washugains.Fragment.InfoFragments.DisplayInfoFragment
import com.example.washugains.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.edit_info_fragment.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditInfoFragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var updateButton : Button
    private lateinit var logoutButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        var feet = arguments!!.getInt("feet")
        var inches = arguments!!.getInt("inches")
        var weight = arguments!!.getInt("weight")
        var calorieGoal = arguments!!.getInt("calorieGoal")
        var username = arguments!!.getString("username")

        logoutButton = logout
        updateButton = myInfoInput

        logoutButton.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val intent = Intent(context, LoginPage::class.java)
            startActivity(intent)
        }

        cancel.setOnClickListener{
            val fragment = DisplayInfoFragment()
            var bundle = Bundle()
            bundle.putString("username", username)
            bundle.putInt("feet",feet.toInt())
            bundle.putInt("inches",inches.toInt())
            bundle.putInt("weight", weight.toInt())
            bundle.putInt("calorieGoal", calorieGoal.toInt())
            fragment.arguments = bundle
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
            Toast.makeText(context, "update cancelled", Toast.LENGTH_SHORT).show()

        }

        updateButton.setOnClickListener{
            var feet = myFeetInput.text.toString()
            var inches = myInchInput.text.toString()
            var weight = myWeightInput.text.toString()
            var calorieGoal = myCaloriesInput.text.toString()

            if (feet != "" &&  inches != "" && weight != "" && calorieGoal != "") {

                db.collection("users").whereEqualTo("username", username).get()
                    .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                val reference = db.collection("users").document(document.id)
                                reference.update("calorieGoal", calorieGoal).addOnSuccessListener {
                                    println("calorieGoal updated")
                                }
                                reference.update("feet", feet).addOnSuccessListener {
                                    println("height (feet) updated")
                                }
                                reference.update("inches", inches).addOnSuccessListener {
                                    println("height (inches) updated")
                                }
                                reference.update("weight", weight).addOnSuccessListener {
                                    println("weight updated")
                                }

                                val fragment = DisplayInfoFragment()
                                var bundle = Bundle()
                                bundle.putString("username", username)
                                bundle.putInt("feet",feet.toInt())
                                bundle.putInt("inches",inches.toInt())
                                bundle.putInt("weight", weight.toInt())
                                bundle.putInt("calorieGoal", calorieGoal.toInt())
                                fragment.arguments = bundle
                                val transaction = activity!!.supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.container, fragment)
                                transaction.commit()

                                Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()

                                myWeightInput.text.clear()
                                myCaloriesInput.text.clear()
                                myFeetInput.text.clear()
                                myInchInput.text.clear()
                            }
                        }
                    })


            }
            else{
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_LONG).show()
            }
        }

    }
}

