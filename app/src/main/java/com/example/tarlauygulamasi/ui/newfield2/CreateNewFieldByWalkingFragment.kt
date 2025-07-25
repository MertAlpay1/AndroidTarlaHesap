package com.example.tarlauygulamasi.ui.newfield2

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.BitmapRegionDecoder
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.databinding.FragmentCreateNewFieldBinding
import com.example.tarlauygulamasi.databinding.FragmentCreateNewFieldByWalkingBinding
import com.example.tarlauygulamasi.ui.newfield.CreateNewFieldViewModel
import com.example.tarlauygulamasi.util.FieldEditStack
import com.example.tarlauygulamasi.util.toFormattedMeter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.common.base.Objects
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ui.IconGenerator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Stack
import kotlin.coroutines.resume
import kotlin.getValue

@AndroidEntryPoint
class CreateNewFieldByWalkingFragment : Fragment(),OnMapReadyCallback{

    private var _binding: FragmentCreateNewFieldByWalkingBinding? = null
    private val binding get()=_binding!!

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var latLngList: MutableList<LatLng>
    private lateinit var  markerList: MutableList<Marker?>
    private lateinit var allFields: Flow<List<Field>>

    private lateinit var currentLatLngList : MutableList<LatLng>

    private lateinit var allPolylines : MutableList<Polyline>
    private var currentPolyline: Polyline? = null
    private var areaPolygon: Polygon? =null

    private var undoStack= Stack<FieldEditStack>()

    private var lastPoint: LatLng? = null
    private var lastLocation: LatLng? = null

    private var isDrawn=false

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private  var area: Double=0.0

    private val viewModel: CreateNewFieldByWalkingViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        latLngList = mutableListOf()
        markerList = mutableListOf()
        allPolylines= arrayListOf()
        currentLatLngList=mutableListOf()

        _binding= FragmentCreateNewFieldByWalkingBinding.inflate(inflater,container,false)

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)
        val view=binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        updateLocation()


        binding.addPoint.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1000)
                return@setOnClickListener
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->

                val userLatLng = LatLng(location.latitude, location.longitude)

                //İlk nokta hariç alınmaz
                if(latLngList.isNotEmpty() && (latLngList.contains(userLatLng) || latLngList.first().equals(userLatLng))){

                    Toast.makeText(requireContext(),"Aynı noktayı seçemezsiniz", Toast.LENGTH_SHORT).show()

                    return@addOnSuccessListener
                }

                Toast.makeText(requireContext(),"Başarıyla eklendi", Toast.LENGTH_SHORT).show()

                latLngList.add(userLatLng)

                currentLatLngList.clear()

                currentLatLngList.add(userLatLng)

                undoStack.push(FieldEditStack.AddPoint(userLatLng,null))

                lastPoint=userLatLng


                currentPolyline?.let {
                    allPolylines.add(it)
                }

                drawAllPolyLine()

                currentPolyline?.remove()
                currentPolyline = null

            }

        }
        binding.undoButton.setOnClickListener {

            if (undoStack.isNotEmpty()) {

                undoStack.pop()

                if (latLngList.isNotEmpty()) {
                    var lastIndex = latLngList.size - 1

                    latLngList.removeAt(lastIndex)

                    if(latLngList.isNotEmpty()){
                        //En son noktayı işlemlere al
                        lastIndex=latLngList.size - 1
                        currentLatLngList.clear()
                        lastPoint=latLngList[lastIndex]
                        currentLatLngList.add(lastPoint!!)
                    }else{
                        currentLatLngList.clear()
                        lastPoint=null
                    }

                    //En sonuncu line sil
                    if (allPolylines.isNotEmpty()) {
                        val removedLine = allPolylines.removeAt(allPolylines.size-1)
                        removedLine.remove()
                    }

                    isDrawn=false
                    areaPolygon?.remove()

                    currentPolyline?.remove()
                    drawAllPolyLine()
                }

            }else{
                findNavController().navigate(R.id.action_createNewFieldByWalkingFragment_to_homeFragment)
            }

        }

        binding.drawButton.setOnClickListener {
            drawPolygonAndAreaCalc()

        }

        binding.saveButton.setOnClickListener {


            //Veri tabanına kaydet
            viewLifecycleOwner.lifecycleScope.launch {

                if(!(latLngList.size<3)){

                     if (!isDrawn) {
                            Toast.makeText(
                                requireContext(), "Lütfen haritadan tarlanızı oluşturun",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val input = saveConfirmationDialog()

                            if (input.isEmpty() || input.isBlank()) {
                                Toast.makeText(
                                    requireContext(), "Lütfen tarlanızı isimlendirin",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.insertField(input, area, latLngList)
                                Toast.makeText(
                                    requireContext(), "Tarlanız başarıyla kaydedildi.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                findNavController().navigate(R.id.action_createNewFieldByWalkingFragment_to_homeFragment)

                            }
                    }
                }else{
                    Toast.makeText(
                        requireContext(), "Lütfen 3 veya daha fazla nokta belirleyin.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

        }


    }

    override fun onMapReady(gMap: GoogleMap) {

        googleMap=gMap

        getCurrentLocationAndMoveCamera()
    }

    fun drawAllPolyLine(){

        if (latLngList.size < 2) {

            return
        }

        allPolylines.forEach { it.remove() }

        currentPolyline?.remove()
        allPolylines.clear()

        for (i in 0 until latLngList.size - 1) {
            val polylineOptions = PolylineOptions()
                .add(latLngList[i])
                .add(latLngList[i + 1])
                .color(Color.BLACK)

            val polyline = googleMap.addPolyline(polylineOptions)
            allPolylines.add(polyline)
        }

    }

    fun updateAndDrawCurrentPolyline(){

        if (currentLatLngList.size < 2){

            return
        }

        currentPolyline?.remove()

        val polylineOption= PolylineOptions().addAll(currentLatLngList).color(Color.BLACK).clickable(false)

        currentPolyline=googleMap.addPolyline(polylineOption)

        val distance = SphericalUtil.computeDistanceBetween(currentLatLngList[0], currentLatLngList[1]).toFormattedMeter()

        binding.diffBetweenPoints.setText(distance)


    }


    fun drawPolygonAndAreaCalc(){

        if(latLngList.size<3){
            Toast.makeText(
                requireContext(), "Lütfen 3 veya daha fazla nokta belirleyin.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        allPolylines.forEach { it.remove() }
        areaPolygon?.remove()

        val polygonOptions=PolygonOptions().addAll(latLngList).clickable(true)
        areaPolygon=googleMap.addPolygon(polygonOptions)

        //Saydam mavi
        areaPolygon?.fillColor = Color.argb(88, 0, 0, 255)

        area= SphericalUtil.computeArea(latLngList)
        val donumArea=area/1000

        isDrawn=true

        binding.areaDonum.text = String.format("%.2f dönüm", donumArea)
    }

    fun drawMarker(){

        markerList.forEach { marker ->
            marker?.remove()
        }

        for(i in 0 until  latLngList.size-1){

            val distance = SphericalUtil.computeDistanceBetween(latLngList[i], latLngList[i+1]).toFormattedMeter()
            val lat:Double=(latLngList[i].latitude+latLngList[i+1].latitude)/2
            val lng:Double=(latLngList[i].longitude+latLngList[i+1].longitude)/2

            val iconGenerator= IconGenerator(requireContext())
            iconGenerator.setColor(Color.RED)
            iconGenerator.setTextAppearance(R.style.markerTextStyle)

            val icon=iconGenerator.makeIcon("aaa")

            val latLng= LatLng(lat,lng)

            val markerOptions= MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .snippet(distance)

            googleMap.addMarker(markerOptions)





        }


    }


    fun updateLocation(){
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }

        locationRequest= LocationRequest.Builder(1000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {

                val location = result.lastLocation ?: return
                val userLatLng = LatLng(location.latitude, location.longitude)

                lastLocation=userLatLng

                //Kontrol eklenebilir ilk nokta seçmeden önce başlamasın diye Flag eklenebilir boolean tarzı ilk nokta koyulduktan sonra
                if(currentLatLngList.isNotEmpty()&&lastPoint!=null){

                    currentLatLngList.clear()

                    currentLatLngList = mutableListOf(lastPoint!!, userLatLng)

                    updateAndDrawCurrentPolyline()

                }

            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }

    fun getCurrentLocationAndMoveCamera(){

        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)
                val camPos = CameraPosition.Builder()
                    .target(userLatLng)
                    .zoom(18F)
                    .build()
                val camUpd = CameraUpdateFactory.newCameraPosition(camPos)
                googleMap.animateCamera(camUpd)

                googleMap.isMyLocationEnabled = true
            }
        }
    }


    suspend fun saveConfirmationDialog(): String = suspendCancellableCoroutine{ cont ->


        val builder= AlertDialog.Builder(requireContext())
        builder.setMessage("Kaydetmek istediğinize emiz misiniz?")
        builder.setTitle("Tarla Adı")

        val input= EditText(requireContext())
        input.hint="Tarla adı girin"

        builder.setView(input)
        builder.setPositiveButton("Evet") { dialog, which ->

            cont.resume(input.text.toString())
            dialog.dismiss()

        }
        builder.setNegativeButton("Hayır"){ dialog, which ->
            cont.resume("")
            dialog.dismiss()
        }
        builder.create()

        builder.show()
    }



    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        binding.map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }



}