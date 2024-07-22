package com.example.trackingdesign

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
