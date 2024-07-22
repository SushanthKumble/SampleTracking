package com.example.trackingdesign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                val newLocation = repository.getNextLocationUpdate()
                if (newLocation != null) {
                    route.add(newLocation)
                    _currentRoute.postValue(route) // Update the route
                } else {
                    // Stop the updates if no more locations are available
                    break
                }
                delay(5000L) // Wait for 5 seconds
            }
        }
    }
}
