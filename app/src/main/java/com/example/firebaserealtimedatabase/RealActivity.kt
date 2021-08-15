package com.example.firebaserealtimedatabase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaserealtimedatabase.Adapters.UserAdapter
import com.example.firebaserealtimedatabase.Models.User
import com.example.firebaserealtimedatabase.databinding.ActivityRealBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RealActivity : AppCompatActivity() {

    private val TAG = "RealActivity"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase:FirebaseDatabase
    lateinit var referense:DatabaseReference //pathlar bilan ishlashga yordam beradi

    lateinit var binding:ActivityRealBinding
    lateinit var userAdapter: UserAdapter
    var list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        firebaseDatabase = FirebaseDatabase.getInstance()
        referense = firebaseDatabase.getReference("users")

        val email = currentUser?.email
        val displayName = currentUser?.displayName
        val phoneNumber = currentUser?.phoneNumber
        val photoUri = currentUser?.photoUrl
        val uid = currentUser?.uid

        val user = User(email, displayName, phoneNumber, photoUri.toString(), uid)


        // addValueEventListener - har doim ishlab turadi
        // addListenerForSingleValueEvent - bir marta ishlaydi
        referense.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val filterList = arrayListOf<User>()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(User::class.java)

                    if (value != null && uid != value.uid) {
                        list.add(value)
                    }
                    if (value!=null && value.uid==uid){
                        filterList.add(value)
                    }
                }
                if (filterList.isEmpty()){
                    referense.child(uid!!).setValue(user)
                }

                userAdapter = UserAdapter(list, object : UserAdapter.OnItemCLickListener{
                    override fun onCLick(user: User) {
                        val intent = Intent(this@RealActivity, MessageActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                })

                binding.userRv.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}