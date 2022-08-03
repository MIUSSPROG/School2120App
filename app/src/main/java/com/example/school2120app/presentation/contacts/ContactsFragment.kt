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
import androidx.navigation.fragment.findNavController
import com.example.school2120app.R
import com.example.school2120app.core.util.ActionListener
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentContactsBinding
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.presentation.contacts.ContactsDetailFragment.Companion.EXTRA_CONTACT_SELECTED
import com.example.school2120app.presentation.contacts.ContactsDetailFragment.Companion.REQUEST_CODE
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
    private val adapter by lazy { ContactsAdapter(object : ActionListener<ContactInfo> {
        override fun onItemClicked(item: ContactInfo, view: View?) {
            val action = ContactsFragmentDirections.actionContactsFragmentToContactsDetailFragment(item)
            findNavController().navigate(action)
        }
    }) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentContactsBinding.bind(view)
        MapKitFactory.initialize(context)
//        initPlacesCoordinates()

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
                        it.data?.let { it1 -> initPlacesCoordinates(it1) }
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

        parentFragmentManager.setFragmentResultListener(REQUEST_CODE, viewLifecycleOwner){ _, data ->
            val contactInfo =  data.get(EXTRA_CONTACT_SELECTED) as ContactInfo
            binding.mapView.map.move(CameraPosition(
                Point(contactInfo.lat, contactInfo.lon), 17.5f, 0.0f, 0.0f)
                , Animation(Animation.Type.SMOOTH, 0f), null)
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

    private fun initPlacesCoordinates(coords: List<ContactInfo>){

        binding.mapView.map.move(
            CameraPosition(Point(coords[0].lat, coords[0].lon), 13.5f, 0.0f, 0.0f) ,
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

        val markerSchool = ImageProvider.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_school))
        val markerKindergarten = ImageProvider.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_kindergarten))

        for (item in coords) {
            val marker = if (item.buildingType == SCHOOL) markerSchool else markerKindergarten
            binding.mapView.map.mapObjects.addPlacemark(Point(item.lat, item.lon), marker)
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
        const val SCHOOL = "Школа"
        const val KINDERGARTEN = "Садик"
    }
}