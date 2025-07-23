package com.example.tarlauygulamasi.ui.newfield

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.databinding.FragmentCreateNewFieldBinding
import com.example.tarlauygulamasi.util.FieldEditStack
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Stack
import android.Manifest
import android.app.Activity
import kotlin.coroutines.resume


@AndroidEntryPoint
class CreateNewFieldFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentCreateNewFieldBinding?=null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var latLngList: MutableList<LatLng>
    private lateinit var  markerList: MutableList<Marker?>
    private lateinit var allFields: Flow<List<Field>>

    private var undoStack= Stack<FieldEditStack>()
    private  var  polygon: Polygon? = null
    private lateinit var polygonList:ArrayList<Polygon>
    private var isDrawn=false
    private  var area: Double=0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    private val viewModel: CreateNewFieldViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        latLngList = mutableListOf()
        markerList = mutableListOf()
        polygonList= arrayListOf()


        _binding = FragmentCreateNewFieldBinding.inflate(inflater, container, false)


        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        val view=binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {

            allFields=viewModel.getAllField()
            drawAllFields()

        }

        binding.drawButton.setOnClickListener {
            isDrawn=true
            draw()
        }
        binding.undoButton.setOnClickListener {

            if(undoStack.isNotEmpty()){

                when(val action=undoStack.pop()){
                    is FieldEditStack.AddPoint ->{

                        if (latLngList.isNotEmpty() && markerList.isNotEmpty()) {
                            val lastIndex = latLngList.size - 1

                            val removedMarker = markerList.removeAt(lastIndex)
                            removedMarker?.remove()

                            latLngList.removeAt(lastIndex)
                        }

                    }
                    is FieldEditStack.MovePoint ->{

                        markerList[action.index]?.position =action.oldPoint
                        latLngList[action.index]=action.oldPoint

                    }
                }
                if(latLngList.size<1) isDrawn=false

                if(isDrawn) draw()


            }else{

                findNavController().navigate(R.id.action_createNewFieldFragment_to_homeFragment)

            }

        }

        binding.saveButton.setOnClickListener {


            //Veri tabanına kaydet
            viewLifecycleOwner.lifecycleScope.launch {

                if(!(latLngList.size<3)){

                if (check()) {

                    //val field= Field()
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

                            findNavController().navigate(R.id.action_createNewFieldFragment_to_homeFragment)

                        }

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



    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(gMap: GoogleMap){

        googleMap = gMap

        googleMap.setOnMapClickListener {latLng ->
            val markerOptions= MarkerOptions().position(latLng).draggable(true)

            val marker =googleMap.addMarker(markerOptions)

            latLngList.add(latLng)
            markerList.add(marker)

            undoStack.push(FieldEditStack.AddPoint(latLng,marker))

        }

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            lateinit var oldLatLng : LatLng
            lateinit var newLatLng : LatLng

            override fun onMarkerDragStart(marker: Marker) {
                //oldLatLng=marker.position
            }

            override fun onMarkerDrag(marker: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {

                newLatLng=marker.position
                val indexa:Int = markerList.indexOf(marker)
                if(indexa!=-1 && newLatLng!=null) {

                    oldLatLng=latLngList[indexa]
                    latLngList[indexa]= newLatLng

                    undoStack.push(FieldEditStack.MovePoint(oldLatLng,newLatLng,indexa))

                    if(isDrawn) draw()


                }
            }

        })

        getCurrentLocation()
     }


    fun draw(){
        polygon?.remove()

        val polygonOptions=PolygonOptions().addAll(latLngList).clickable(true)
        polygon=googleMap.addPolygon(polygonOptions)

        //Saydam mavi
        polygon?.fillColor = Color.argb(88, 0, 0, 255)

        area= SphericalUtil.computeArea(latLngList)
        val donumArea=area/1000

        binding.area.text = String.format("%.2f m²", area)
        binding.areaDonum.text = String.format("%.2f dönüm", donumArea)
    }

    suspend fun drawAllFields(){

        val listAF: List<Field> = allFields.first()

        listAF.forEach {

            val polygonOptions=PolygonOptions().addAll(it.pointList).clickable(true)
            //locale polygon
            var polygon=googleMap.addPolygon(polygonOptions)

            polygonList.add(polygon)

            //Saydam kirmiz
            polygon?.fillColor = Color.argb(88, 255, 0, 0)

        }

    }

    fun ccw(A: LatLng, B:LatLng, C:LatLng):Boolean{
        return (C.latitude-A.latitude)*(B.longitude-A.longitude) > (B.latitude-A.latitude)*(C.longitude-A.longitude)
    }
    fun intersect(A: LatLng, B:LatLng, C:LatLng,D: LatLng):Boolean{
        return ccw(A,C,D)!=ccw(B,C,D) && ccw(A,B,C)!=ccw(A,B,D)
    }

    suspend fun check(): Boolean{

        //Kesişim var mı
        val listAF: List<Field> = allFields.first()

        for(x in latLngList.indices){

            val a1 = latLngList[x]
            val a2 = latLngList[(x + 1) % latLngList.size]

            for(y in listAF.indices){

                val aFLatLng=listAF.get(y)
                val aFPointList=aFLatLng.pointList

                for(z in aFPointList.indices){

                    val b1 = aFPointList[z]
                    val b2 = aFPointList[(z + 1) % aFPointList.size]

                    if(intersect(a1,a2,b1,b2)){
                        Toast.makeText(requireContext(), "Tarlanız başka bir tarla ile kesiştiğinden kaydedilemedi.", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
        }

        //Tarlalar iç içe geçiyor mu
        for (polygons in polygonList){

            var pointsInPolgon :List<LatLng> = polygons.points

            for (point in pointsInPolgon){

                if(PolyUtil.containsLocation(point, polygon?.points,true)){

                    Toast.makeText(requireContext(), "Tarlanız başka bir tarlayı içine alamaz.", Toast.LENGTH_SHORT).show()

                    return false
                }

            }

        }



        return true
    }

    fun getCurrentLocation(){

        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),101)

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
        binding.map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

}

