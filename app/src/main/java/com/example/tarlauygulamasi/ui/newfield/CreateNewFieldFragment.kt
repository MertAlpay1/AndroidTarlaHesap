package com.example.tarlauygulamasi.ui.newfield

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.location.GnssAntennaInfo
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColor
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tarlauygulamasi.R
import com.example.tarlauygulamasi.data.locale.entity.Field
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.checkerframework.checker.units.qual.Area
import kotlin.coroutines.resume
import kotlin.math.log

//Başlangıçta kullanıcının konumu focus olarak başlatılabilir
@AndroidEntryPoint
class CreateNewFieldFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentCreateNewFieldBinding?=null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var LatLngList: MutableList<LatLng>
    private lateinit var  markerList: MutableList<Marker?>
    private  var  polygon: Polygon? = null
    private var isDrawn=false
    private  var area: Double=0.0


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
            viewLifecycleOwner.lifecycleScope.launch {

                //val field= Field()
                if(!isDrawn){
                    Toast.makeText(requireContext(),"Lütfen haritadan tarlanızı oluşturun",
                        Toast.LENGTH_SHORT).show()
                }else {
                    val input = saveConfirmationDialog()

                    /*
                    if (input.isEmpty()) {
                        Toast.makeText(requireContext(),"Lütfen tarlanızı isimlendirin",
                            Toast.LENGTH_SHORT).show()
                    }
                    else {
                        viewModel.insertField(input, area, LatLngList)
                    }

                     */
                    viewModel.insertField(input, area, LatLngList)
                }
                //Yönlendirme yap
            }


        }


    }


    @SuppressLint("PotentialBehaviorOverride")
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

         area= SphericalUtil.computeArea(LatLngList)
        val donumArea=area/1000

        binding.area.text = String.format("%.2f m²", area)
        binding.areaDonum.text = String.format("%.2f dönüm", donumArea)
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


}

