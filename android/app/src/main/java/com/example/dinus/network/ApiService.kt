package com.example.dinus.network

import com.example.dinus.model.Community
import com.example.dinus.model.CommunityMessage
import com.example.dinus.model.Doctor
import com.example.dinus.model.DoctorMessege
import com.example.dinus.model.Event
import com.example.dinus.model.EventMessege
import com.example.dinus.model.Message
import com.example.dinus.model.Mood
import com.example.dinus.model.PredictRequest
import com.example.dinus.model.PredictResponse
import com.example.dinus.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/register")
    fun register(@Body user: User): Call<User>

    @POST("auth/login")
    fun login(@Body user: User): Call<User>

    @GET("auth/users")
    fun getUsers(): Call<List<User>>

    @GET("chat/{receiver_id}")
    fun getChatMessages(
        @Path("receiver_id") receiverId: Int,
        @Query("sender_id") senderId: Int
    ): Call<List<Message>>

    @POST("chat/send")
    fun sendMessage(@Body message: Message): Call<Message>

    @GET("events")
    fun getEvents(): Call<List<Event>>

    @GET("events/{id}")
    fun getEventById(@Path("id") eventId: Int): Call<Event>

    @GET("chat-events/{receiver_id}")
    fun getChatEventMessages(
        @Path("receiver_id") receiverId: Int,
        @Query("sender_id") senderId: Int
    ): Call<List<EventMessege>>

    @POST("chat-events/send")
    fun sendMessage(@Body message: EventMessege): Call<EventMessege>

    @GET("doctors")
    fun getDoctors(): Call<List<Doctor>>

    @GET("doctor-chat/{receiverId}")
    fun getDoctorMessages(
        @Path("receiverId") receiverId: Int,
        @Query("sender_id") senderId: Int
    ): Call<List<DoctorMessege>>

    @POST("doctor-chat/send")
    fun sendMessage(@Body message: DoctorMessege): Call<DoctorMessege>

    @GET("communities")
    fun getCommunities(): Call<List<Community>>

    @GET("community-messages/{communityId}")
    fun getCommunityMessages(
        @Path("communityId") communityId: Int,
        @Query("sender_id") senderId: Int
    ): Call<List<CommunityMessage>>

    @POST("community-messages")
    fun sendCommunityMessage(@Body message: CommunityMessage): Call<CommunityMessage>

    @POST("predict")
    fun predictMood(@Body request: PredictRequest): Call<PredictResponse>


}
