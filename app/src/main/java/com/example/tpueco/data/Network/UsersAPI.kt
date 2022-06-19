package com.example.tpueco.data.Network

import com.example.tpueco.domain.Model.UserDataResponse
import com.example.tpueco.domain.Model.UserTokenResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface UsersAPI {


@GET
@Headers("Content-Type: application/json")
fun getAccessToken(@Url string: String): Single<UserTokenResponse>


    @GET
    @Headers("Content-Type: application/json")
    fun getUserData(@Url string: String): Single<UserDataResponse>

}