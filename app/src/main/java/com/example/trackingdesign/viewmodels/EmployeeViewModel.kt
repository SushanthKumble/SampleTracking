package com.example.trackingdesign.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingdesign.repository.LocationRepository
import com.example.trackingdesign.data.LocationUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _currentRoute = MutableLiveData<List<LocationUpdate>>()
    val currentRoute: LiveData<List<LocationUpdate>> = _currentRoute

    private val route = mutableListOf<LocationUpdate>()

    init {
        fetchLocationUpdates()
    }

    private fun fetchLocationUpdates() {
        viewModelScope.launch {
            while (true) {
                val nextLocation = repository.getNextLocationUpdate()
                nextLocation?.let {
                    route.add(it)
                    _currentRoute.postValue(route)
                }
                delay(10000) // 10sec
            }
        }
    }
}

//replace the above code with the below when api is ready

//class EmployeeViewModel(private val repository: UserLocationRepository) : ViewModel() {
//
//    private val _currentRoute = MutableLiveData<NetworkResult<List<LocationUpdate>>>()
//    val currentRoute: LiveData<NetworkResult<List<LocationUpdate>>> get() = _currentRoute
//
//    fun fetchUserLocations() {
//        viewModelScope.launch {
//            _currentRoute.value = NetworkResult.Loading()
//            val result = repository.getUserLocations()
//            _currentRoute.value = result
//        }
//    }
//}

