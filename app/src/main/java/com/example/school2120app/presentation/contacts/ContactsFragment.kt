package com.example.school2120app.presentation.contacts

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentContactsBinding
import com.example.school2120app.domain.model.contacts.BuildingType.*
import com.example.school2120app.domain.model.contacts.ContactMapCoordinate
import com.example.school2120app.presentation.contacts.adapter.ContactsAdapter
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ContactsFragment: Fragment(R.layout.fragment_contacts) {

    private lateinit var binding: FragmentContactsBinding
    private val viewModel: ContactsViewModel by viewModels()
    private val adapter by lazy { ContactsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentContactsBinding.bind(view)
        MapKitFactory.initialize(context)
        initPlacesCoordinates()

        binding.apply {
            rvContacts.adapter = adapter

            viewModel.getContacts()
            viewModel.contactsLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Loading -> {
                        pbContactsLoading.visibility = VISIBLE
                    }
                    is Success -> {
                        pbContactsLoading.visibility = INVISIBLE
                        adapter.submitList(it.data)
//                        Log.d("result", it.data.toString())
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    private fun initPlacesCoordinates(){
        val coords = mutableListOf<ContactMapCoordinate>()
        coords.addAll(
            listOf(
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building1_lat).toDouble(),
                    long = resources.getString(R.string.building1_lon).toDouble(),
                    address = resources.getString(R.string.building1),
                    type = SCHOOL),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building2_lat).toDouble(),
                    long = resources.getString(R.string.building2_lon).toDouble(),
                    address = resources.getString(R.string.building2),
                    type = SCHOOL),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building3_lat).toDouble(),
                    long = resources.getString(R.string.building3_lon).toDouble(),
                    address = resources.getString(R.string.building3),
                    type = SCHOOL),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building4_lat).toDouble(),
                    long = resources.getString(R.string.building4_lon).toDouble(),
                    address = resources.getString(R.string.building4),
                    type = SCHOOL),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building5_lat).toDouble(),
                    long = resources.getString(R.string.building5_lon).toDouble(),
                    address = resources.getString(R.string.building5),
                    type = KINDERGARTEN),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building6_lat).toDouble(),
                    long = resources.getString(R.string.building6_lon).toDouble(),
                    address = resources.getString(R.string.building6),
                    type = KINDERGARTEN),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building7_lat).toDouble(),
                    long = resources.getString(R.string.building7_lon).toDouble(),
                    address = resources.getString(R.string.building7),
                    type = KINDERGARTEN),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building8_lat).toDouble(),
                    long = resources.getString(R.string.building8_lon).toDouble(),
                    address = resources.getString(R.string.building8),
                    type = KINDERGARTEN),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building9_lat).toDouble(),
                    long = resources.getString(R.string.building9_lon).toDouble(),
                    address = resources.getString(R.string.building9),
                    type = KINDERGARTEN),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building10_lat).toDouble(),
                    long = resources.getString(R.string.building10_lon).toDouble(),
                    address = resources.getString(R.string.building10),
                    type = KINDERGARTEN),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building11_lat).toDouble(),
                    long = resources.getString(R.string.building11_lon).toDouble(),
                    address = resources.getString(R.string.building11),
                    type = KINDERGARTEN),
                ContactMapCoordinate(
                    lat = resources.getString(R.string.building12_lat).toDouble(),
                    long = resources.getString(R.string.building12_lon).toDouble(),
                    address = resources.getString(R.string.building12),
                    type = KINDERGARTEN)
            )
        )

        binding.mapView.map.move(
            CameraPosition(Point(coords[0].lat, coords[0].long), 13.5f, 0.0f, 0.0f) ,
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

        val markerSchool = ImageProvider.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_school))
        val markerKindergarten = ImageProvider.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_kindergarten))

        for (item in coords) {
            val marker = if (item.type == SCHOOL) markerSchool else markerKindergarten
            binding.mapView.map.mapObjects.addPlacemark(Point(item.lat, item.long), marker)
        }

    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(requireContext(), drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    companion object{
        const val YANDEX_MAP_API_KEY = "00853820-db0b-432b-b76d-cef71a379ae4"
    }
}