package com.example.firebaserealtimedatabase.Retrofit

import com.example.firebaserealtimedatabase.Retrofit.Model.MyResponce
import com.example.firebaserealtimedatabase.Retrofit.Model.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers(
        "Content-type:application/json",
        "Authorization:key=AAAAaixKPT8:APA91bFt45iy_NpnihXLq8mG92BPnnXYJeZ5XPeFb5krVTPwfJCnLXzRDMq2mdbJ_ktsVuplsVblffroF9jAMZeQHwp7rGIavARbR2AgNE4etQFufVotUMGUAiI0li9w3iSLADdix1DF"
    )
    @POST("fcm/send")
    fun sendNotification(@Body sender: Sender): Call<MyResponce>
}