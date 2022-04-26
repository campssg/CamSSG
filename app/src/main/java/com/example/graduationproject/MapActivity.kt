package com.example.graduationproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivityMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback



class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object{

                lateinit var naverMap: NaverMap
private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

    }

    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }


    override fun onMapReady(p0: NaverMap) {
        MapActivity.naverMap = naverMap
        var camPos = CameraPosition(
            LatLng(34.38,128.55),
            9.0
        )
        naverMap.cameraPosition = camPos
    }
}