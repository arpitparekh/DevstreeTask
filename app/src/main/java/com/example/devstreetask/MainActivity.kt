package com.example.devstreetask

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.devstreetask.databinding.ActivityMainBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class MainActivity : AppCompatActivity(),OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    lateinit var binding : ActivityMainBinding
    private lateinit var mMap: GoogleMap
    lateinit var myPlaces: MyPlaces
    var updatedObj : MyPlaces? = null
    lateinit var placesDao: PlacesDao
    var latLongList = ArrayList<LatLng>()
    var bool = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        placesDao = PlacesDatabase.getInstance(this)!!.getPlacesDao()

        val apiKey = getString(R.string.my_map_api_key)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        val placesClient = Places.createClient(this)

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        autocompleteFragment!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                addMarker(place)
            }

            override fun onError(status: Status) {

            }
        })

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                }
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                } else -> {
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION))

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnAdd.setOnClickListener {

            if(bool){
                updatedObj?.long = myPlaces.long
                updatedObj?.lat = myPlaces.lat
                updatedObj?.address = myPlaces.address
                updatedObj?.name = myPlaces.name
                val intent = Intent(this,ShowPlacesActivity::class.java)
                intent.putExtra("obj",updatedObj)
                startActivity(intent)
                finish()

            }else{
                placesDao.insertPlace(myPlaces)
                startActivity(Intent(this,ShowPlacesActivity::class.java))
                finish()
            }

            bool = false

            }

        }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        if(intent!=null){

            updatedObj = intent.getSerializableExtra("data") as MyPlaces?

            if(updatedObj!=null){
                bool = true
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(updatedObj!!.lat, updatedObj!!.long))
                markerOptions.title(updatedObj!!.address)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                mMap.addMarker(markerOptions)?.showInfoWindow()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(updatedObj!!.lat,
                    updatedObj!!.long), 15f))
            }

            val list : ArrayList<MyPlaces?>? =
                intent.getSerializableExtra("list") as ArrayList<MyPlaces?>?

            Log.i("mydata",list.toString())

            if (list != null) {
                latLongList.clear()
                for(i in list){
                    val latLng = LatLng(i!!.lat,i.long)
                    latLongList.add(latLng)
                }

                mMap.addPolyline(
                    PolylineOptions().addAll(latLongList)
                        .width
                            (5f)
                        .color(Color.RED)
                        .geodesic(true)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongList[0], 13f))

            }

        }
    }

    fun addMarker(p: Place) {

        myPlaces = MyPlaces(p.name,p.address,p.latLng.latitude,p.latLng.longitude)

        val markerOptions = MarkerOptions()
        markerOptions.position(p.latLng)
        markerOptions.title(p.address)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        mMap.addMarker(markerOptions)?.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p.latLng, 15f))

        binding.linear.visibility = View.VISIBLE

        if(intent!=null){
            val list : ArrayList<MyPlaces?>? =
                intent.getSerializableExtra("list") as ArrayList<MyPlaces?>?

            Log.i("mydata",list.toString())

            if (list != null) {
                latLongList.clear()
                for(i in list){
                    val latLng = LatLng(i!!.lat,i.long)
                    latLongList.add(latLng)
                }

                mMap.addPolyline(
                    PolylineOptions().addAll(latLongList)
                        .width
                            (5f)
                        .color(Color.RED)
                        .geodesic(true)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongList[0], 13f))

            }

        }

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return false
    }
}