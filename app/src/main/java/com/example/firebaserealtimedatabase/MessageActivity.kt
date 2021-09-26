package com.example.firebaserealtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebaserealtimedatabase.Adapters.MessageAdapter
import com.example.firebaserealtimedatabase.Models.Message
import com.example.firebaserealtimedatabase.Models.User
import com.example.firebaserealtimedatabase.Retrofit.ApiClient
import com.example.firebaserealtimedatabase.Retrofit.ApiService
import com.example.firebaserealtimedatabase.Retrofit.Model.Data
import com.example.firebaserealtimedatabase.Retrofit.Model.MyResponce
import com.example.firebaserealtimedatabase.Retrofit.Model.Sender
import com.example.firebaserealtimedatabase.databinding.ActivityMessageBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : AppCompatActivity() {
    lateinit var binding:ActivityMessageBinding
    private val TAG = "MessageActivity"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var messageAdapter: MessageAdapter


    lateinit var apiService:ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        val user = intent.getSerializableExtra("user") as User

        apiService = ApiClient.getRetrofit("https://fcm.googleapis.com/").create(ApiService::class.java)

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

            apiService.sendNotification(
                Sender(
                    Data(
                    firebaseAuth.currentUser!!.uid,
                    R.drawable.ic_launcher_foreground,
                    m,
                        "New Message",
                        user.uid ?: ""
                    ),
                    "fG-TZVANTPq1WiBY6QqMgV:APA91bHfmTOr8EY3DNVuUKUFcka_sRSnZQ55UJKmHD8hH7Tw0WAX2oj-w4LJUG-NZfBp1uw6UMEr6r08FCmF4nl03EuE7LlPg4jqY49cEqsb3vmQHUZsjq0HN6vb4MhIYA9ifrhOFGmn"
                )
            ).enqueue(object : Callback<MyResponce>{
                override fun onResponse(call: Call<MyResponce>, response: Response<MyResponce>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@MessageActivity, "Succsefull", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<MyResponce>, t: Throwable) {

                }
            })
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