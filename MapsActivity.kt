package com.example.mapa

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapa.MainActivity.Companion.listaSklepow
import com.example.mapa.databinding.ActivityMapsBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var geoClient: GeofencingClient
    private var id = 60
    private lateinit var database: FirebaseDatabase
    var list = arrayListOf<Shop>()

    var lista = mutableListOf<Shop>()


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = FirebaseDatabase.getInstance()

        geoClient = LocationServices.getGeofencingClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.btDodaj.setOnClickListener {

            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnCompleteListener{
                    val location = it.result
                    val place = LatLng(location.latitude, location.longitude)
                    val geo  = Geocoder(this)
                    val marker = MarkerOptions().position(place).title(binding.etDodaj.text.toString())
                    marker.snippet(geo.getFromLocation(location.latitude, location.longitude, 1)[0].featureName)
                    mMap.addMarker(marker)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place))

                    val desc = binding.etOpis.text.toString()
                    val rad = binding.etRad.text.toString().toFloat()

                    Log.i("rad",rad.toString())

                    CoroutineScope(Dispatchers.IO).launch {
                        dbAdd(marker.title, location.latitude, location.longitude,desc,rad)
                    }

                       addGeo(place,binding.etRad.text.toString().toFloat(),marker.title )
                }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.isMyLocationEnabled = true
        for (l in listaSklepow) {
            val coordinate = LatLng(l.lon,l.lat)
            val marker = MarkerOptions().position(coordinate).title(l.name)
            mMap.addMarker(marker)

            val circleOptions = CircleOptions()
            circleOptions.center(coordinate)
            circleOptions.radius(l.radius.toDouble())
            circleOptions.strokeColor(Color.argb(255,0,255,100))
            circleOptions.fillColor(Color.argb(64,0,255,54))
            circleOptions.strokeWidth(4F)
            mMap.addCircle((circleOptions))
        }

    }


    @SuppressLint("MissingPermission")
    fun addGeo(place: LatLng, rad: Float, sklep: String){

            id = id+1

            val geofence = Geofence.Builder()
                    .setRequestId(sklep)
                    .setCircularRegion(place.latitude, place.longitude, rad)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build()

            val geoRequest = GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build()

            val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    id,
                    Intent(this, GeoReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )


            geoClient.addGeofences(geoRequest, pendingIntent)
                    .addOnSuccessListener { Toast.makeText(this, "Dodano geofence z id: $id", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this, "Geofence nie zostal dodany", Toast.LENGTH_SHORT).show() }

    }

    private suspend fun dbAdd(name: String, lat: Double, lon: Double,description:String,rad:Float){

        var reference = database.getReference("users/keyy/shops")
        var shop = Shop(name = name, lat = lat, lon = lon,desc = description,radius = rad)

        reference.push().setValue(shop)

    }








}

