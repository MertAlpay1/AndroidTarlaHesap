package com.example.tarlauygulamasi.ui.newfield

import android.graphics.Color
import android.location.GnssAntennaInfo
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.databinding.FragmentCreateNewFieldBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.SphericalUtil
import dagger.hilt.EntryPoint

//Kaydet butonu ileride eklenecek kaydetten sorna emin misiniz diye sorulacak evet denilirse tarla
//ismi kullanıcıdan alınacak ve kullanıcı id'si ile firestora kaydedilecek
//Başlangıçta kullanıcının konumu focus olarak başlatılabilir
//Geri tuşu stack mantığında çalışsın
class CreateNewFieldFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentCreateNewFieldBinding?=null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var LatLngList: MutableList<LatLng>
    private lateinit var  markerList: MutableList<Marker?>
    private  var  polygon: Polygon? = null
    private var isDrawn=false

    private val viewModel: CreateNewFieldViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        LatLngList = mutableListOf()
        markerList = mutableListOf()


        _binding = FragmentCreateNewFieldBinding.inflate(inflater, container, false)


        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        val view=binding.root


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.drawButton.setOnClickListener {
            isDrawn=true
            draw()
        }
        binding.undoButton.setOnClickListener {

            if (markerList.isNotEmpty()) {
                val removedMarker = markerList.removeAt(markerList.size - 1)
                removedMarker?.remove()
            }
            if (LatLngList.isNotEmpty()) {
                LatLngList.removeAt(LatLngList.size - 1)
            }

            if(LatLngList.size<1) isDrawn=false

            if(isDrawn) draw()

            if(markerList.isEmpty()){

                findNavController().navigate(R.id.action_createNewFieldFragment_to_homeFragment)
            }

        }

        binding.saveButton.setOnClickListener {

            //Veri tabanına kaydet

        }


    }


    override fun onMapReady(gMap: GoogleMap){

        googleMap = gMap

        googleMap.setOnMapClickListener {latLng ->
            val markerOptions= MarkerOptions().position(latLng).draggable(true)

            val marker =googleMap.addMarker(markerOptions)

            LatLngList.add(latLng)
            markerList.add(marker)
        }

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            var oldLatLng : LatLng?=null
            var newLatLng : LatLng?=null

            override fun onMarkerDragStart(marker: Marker) {
                oldLatLng=marker.position
            }

            override fun onMarkerDrag(marker: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {

                newLatLng=marker.position
                val index = markerList.indexOf(marker)
                if(index!=-1 && newLatLng!=null) {

                    LatLngList[index]= newLatLng!!

                    if(isDrawn) draw()
                }

            }


        })

     }

    fun draw(){
        polygon?.remove()

        val polygonOptions=PolygonOptions().addAll(LatLngList).clickable(true)
        polygon=googleMap.addPolygon(polygonOptions)

        //Saydam mavi
        polygon?.fillColor = Color.argb(88, 0, 0, 255)

        val area= SphericalUtil.computeArea(LatLngList)
        val donumArea=area/1000

        binding.area.text = String.format("%.2f m²", area)
        binding.areaDonum.text = String.format("%.2f dönüm", donumArea)
    }


}

