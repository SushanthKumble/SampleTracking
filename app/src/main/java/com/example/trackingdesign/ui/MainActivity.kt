package com.example.trackingdesign.ui

import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import android.location.Geocoder
import android.location.Address
import android.content.Context
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingdesign.other.Constants.POLYLINE_COLOR
import com.example.trackingdesign.other.Constants.POLYLINE_WIDTH
import com.example.trackingdesign.viewmodels.EmployeeViewModel
import com.example.trackingdesign.data.LocationUpdate
import com.example.trackingdesign.R
import com.example.trackingdesign.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: EmployeeViewModel by viewModels()
    private var map: GoogleMap? = null
    private var pathPoints = mutableListOf<LatLng>()
    private lateinit var binding: ActivityMainBinding
    //Replace the code with txt file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.currentRoute.observe(this) { locations ->
            pathPoints = locations.map { LatLng(it.latitude, it.longitude) }.toMutableList()
            updateMap()
            updateTable(locations)
        }
    }

    private fun updateMap() {
        map?.clear()
        if (pathPoints.isNotEmpty()) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(pathPoints)
            map?.addPolyline(polylineOptions)
            zoomToSeeWholeTrack()

            // Add markers for each location
            for (point in pathPoints) {
                map?.addMarker(MarkerOptions().position(point))
            }
        }
    }

    private fun zoomToSeeWholeTrack() {
        if (pathPoints.isNotEmpty()) {
            val bounds = LatLngBounds.Builder()
            for (point in pathPoints) {
                bounds.include(point)
            }
            map?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds.build(),
                    binding.mapView.width,
                    binding.mapView.height,
                    (binding.mapView.height * 0.05f).toInt()
                )
            )
        }
    }

    private fun updateTable(locations: List<LocationUpdate>) {
        binding.tablayout.removeAllViews()

        // Add table headers
        val headerRow = TableRow(this)
        headerRow.addView(createTableCell("SLNO", true))
        headerRow.addView(createTableCell("LOCATION", true))
        headerRow.addView(createTableCell("TIME", true))
        binding.tablayout.addView(headerRow)

        // Reverse locations to show the most recent first
        val reversedLocations = locations.reversed()

        // Add table rows
        reversedLocations.forEachIndexed { index, locationUpdate ->
            val row = TableRow(this)
            row.addView(createTableCell((index + 1).toString(), false))

            // Get the location name
            val locationName = getAddress(this, locationUpdate.latitude, locationUpdate.longitude)
            row.addView(createTableCell(locationName, false))

            // Format timestamp to only show time
            val formattedTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(locationUpdate.timestamp))
            row.addView(createTableCell(formattedTime, false))

            // Set OnClickListener to show the popup dialog
            row.setOnClickListener {
                showLocationInfoDialog(locationUpdate)
            }

            binding.tablayout.addView(row)
        }
    }



    private fun createTableCell(text: String, isHeader: Boolean): TextView {
        return TextView(this).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f // Distribute space evenly among cells
            }
            setPadding(8, 8, 8, 8)
            this.text = text
            // Optionally style header cells differently
            if (isHeader) {
                setTextColor(resources.getColor(android.R.color.white, null))
                setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
            } else {
                setTextColor(resources.getColor(android.R.color.black, null))
            }
        }
    }


    private fun showLocationInfoDialog(locationUpdate: LocationUpdate) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_location_info, null)

        val latitudeTextView: TextView = dialogView.findViewById(R.id.latitude)
        val longitudeTextView: TextView = dialogView.findViewById(R.id.longitude)
        val addressTextView: TextView = dialogView.findViewById(R.id.address)

        latitudeTextView.text = "Latitude: ${locationUpdate.latitude}"
        longitudeTextView.text = "Longitude: ${locationUpdate.longitude}"
        addressTextView.text = "Address: ${getAddress(this, locationUpdate.latitude, locationUpdate.longitude)}"

        AlertDialog.Builder(this)
            .setTitle("Location Info")
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }



    private fun getAddress(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                // Return the locality, subLocality, or featureName, whichever is available
                address.locality ?: address.subLocality ?: address.featureName ?: "Unknown location"
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            "Unknown location"
        }
    }




    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}
