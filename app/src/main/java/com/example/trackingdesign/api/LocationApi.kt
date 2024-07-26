package com.example.trackingdesign.api

import com.example.trackingdesign.data.LocationUpdate
import retrofit2.http.Body
import retrofit2.http.GET

interface LocationApi {


    //function get the location of the user based on he id should be added here
    @GET("/add_end_point_here")
    suspend fun getLocatinDetails():List<LocationUpdate>


}