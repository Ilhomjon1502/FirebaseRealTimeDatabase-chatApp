package com.example.firebaserealtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaserealtimedatabase.Adapters.MessageAdapter
import com.example.firebaserealtimedatabase.Models.Message
import com.example.firebaserealtimedatabase.Models.User
import com.example.firebaserealtimedatabase.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : AppCompatActivity() {
    lateinit var binding:ActivityMessageBinding

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        val user = intent.getSerializableExtra("user") as User

        binding.sendBtn.setOnClickListener {
            val m = binding.messageEd.text.toString()

            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val date = simpleDateFormat.format(Date())
            val message = Message(m, date, firebaseAuth.currentUser?.uid, user.uid)

            val key = reference.push().key
            reference.child("${firebaseAuth.currentUser?.uid}/messages/${user.uid!!}/$key")
                .setValue(message)

            reference.child("${user.uid}/messages/${firebaseAuth.currentUser?.uid}/$key")
                .setValue(message)
        }

        reference.child("${firebaseAuth.currentUser?.uid}/messages/${user.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<Message>()
                    val children = snapshot.children
                    for (child in children) {
                        val value = child.getValue(Message::class.java)
                        if (value != null) {
                            list.add(value)
                        }
                    }
                    messageAdapter = MessageAdapter(list, firebaseAuth.currentUser?.uid!!)
                    binding.messageRv.adapter = messageAdapter
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}