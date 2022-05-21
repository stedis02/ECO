package com.example.testovoegit.Network

import com.example.testovoegit.Model.UserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UsersAPI {
@GET("users")
@Headers("Content-Type: application/json")
fun GetUserResponse(): Single<List<UserResponse>>
}