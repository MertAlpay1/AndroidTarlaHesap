package com.example.tarlauygulamasi.ui.field_detail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.databinding.FragmentFieldDetailBinding
import com.example.tarlauygulamasi.util.FieldEditStack
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
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Stack

@AndroidEntryPoint
class FieldDetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentFieldDetailBinding?=null
    private val binding get()=_binding!!

    private lateinit var field: Field

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var latLngList: MutableList<LatLng>
    private lateinit var  markerList: MutableList<Marker?>

    private var undoStack= Stack<FieldEditStack.MovePoint>()
    private  var  polygon: Polygon? = null
    private  var area: Double=0.0



    private val viewModel: FieldDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentFieldDetailBinding.inflate(inflater, container, false)

        latLngList = mutableListOf()
        markerList = mutableListOf()

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        val view=binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fieldId=arguments?.getLong("fieldId") ?: -1L
        if(fieldId==-1L){
            Toast.makeText(requireContext(),"Tarla bilgisi alınamadı.",Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            field = viewModel.getField(fieldId)


            latLngList=field.pointList

            if (latLngList.isNotEmpty()) {
                markerList = mutableListOf()
                latLngList.forEach { latLng ->
                    val marker = googleMap.addMarker(
                        MarkerOptions().position(latLng).draggable(true)
                    )
                    markerList.add(marker)
                }
            }

            val areaDonum= field.area / 1000
            val zoomFloat=when{
                areaDonum <10 ->18F
                areaDonum <1000 -> 15F
                areaDonum <10000 -> 12F
                areaDonum <100000 -> 9F
                else ->6F
            }
            val camPos= CameraPosition.Builder().target(latLngList[0])
                .zoom(zoomFloat)
                .build()
            val camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            googleMap.animateCamera(camUpd3);
            draw()
        }

        binding.undoButton.setOnClickListener {
            if (undoStack.isNotEmpty()) {

                val action = undoStack.pop()

                markerList[action.index]?.position = action.oldPoint
                latLngList[action.index] = action.oldPoint

                draw()
            }else{
                findNavController().navigate(R.id.action_fieldDetailFragment_to_homeFragment)
            }
        }

        binding.saveButton.setOnClickListener{
            saveConfirmationDialog()

        }






    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            lateinit var oldLatLng : LatLng
            lateinit var newLatLng : LatLng

            override fun onMarkerDragStart(marker: Marker) {

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

                    draw()
                }

            }


        })


    }


    fun draw(){
        polygon?.remove()

        val polygonOptions=PolygonOptions().addAll(latLngList).clickable(true)
        polygon=googleMap.addPolygon(polygonOptions)

        //Saydam mavi
        polygon?.fillColor = Color.argb(88, 0, 255, 0)

        area= SphericalUtil.computeArea(latLngList)

        binding.area.text = String.format("%.2f m²", area)
        binding.areaDonum.text = String.format("%.2f dönüm", area/1000)
    }

    private fun saveConfirmationDialog(){

        val builder=AlertDialog.Builder(requireContext())
        builder.setTitle("Değişiklikleri Kaydet")
        builder.setMessage("Değişiklikleri kaydetmek istediğinize emin misiniz?")

        builder.setPositiveButton("Evet"){dialog, which ->

            field.pointList=latLngList
            field.area=area

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateField(field)
            }
            Toast.makeText(requireContext(),"Başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
            undoStack.clear()
            dialog.dismiss()

        }

        builder.setNegativeButton("Hayır"){dialog, which ->
            dialog.dismiss()
        }
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