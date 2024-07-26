package com.example.trackingdesign.repository

import com.example.trackingdesign.data.LocationUpdate
import javax.inject.Inject

class LocationRepository @Inject constructor() {

    private val locations = listOf(
        LocationUpdate(12.588247, 74.957962),
        LocationUpdate(12.588750, 74.959099),
        LocationUpdate(12.589189, 74.960902),
        LocationUpdate(12.589608, 74.962168),
        LocationUpdate(12.590446, 74.963884),
        LocationUpdate(12.590655, 74.964871),
        LocationUpdate(12.590718, 74.966180),
        LocationUpdate(12.590676, 74.967296),
        LocationUpdate(12.590655, 74.967918),
        LocationUpdate(12.590613, 74.968755),

    )

    private var index = 0

    fun getNextLocationUpdate(): LocationUpdate? {
        return if (index < locations.size) {
            locations[index++]
        } else {
            null // Return null if the end of the list is reached
        }
    }
}


//change the repository to following to real implementation

//class UserLocationRepository(private val apiService: ApiService) {
//
//    suspend fun getUserLocations(): NetworkResult<List<LocationUpdate>> {
//        return try {
//            val response = apiService.getUserLocations()
//            NetworkResult.Success(response)
//        } catch (e: Exception) {
//            NetworkResult.Error(e.message ?: "An unknown error occurred")
//        }
//    }
//}

