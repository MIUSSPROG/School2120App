package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.school2120app.R
import com.example.school2120app.databinding.FragmentProfileDocumentsBinding
import com.example.school2120app.domain.model.profile.ProfileDocs
import com.example.school2120app.prefs
import com.example.school2120app.presentation.profile.adapter.ProfileViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ProfileDocumentsFragment: Fragment(R.layout.fragment_profile_documents) {

    private lateinit var binding: FragmentProfileDocumentsBinding
    private val fragTitles = listOf(SIGNED, UNSIGNED)
    private val args: ProfileDocumentsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileDocumentsBinding.bind(view)

        val profileInfo = args.profileDocs!!
        val fragList = listOf(
            DocumentSubscriptionFragment(profileInfo.subscribedDocs),
            DocumentSubscriptionFragment(profileInfo.unsubscribedDocs)
        )
        val adapter = ProfileViewPagerAdapter(requireActivity(), fragList)
        binding.apply {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager){ tab, pos ->
                tab.text = fragTitles[pos]
            }.attach()

            tvStaffName.text = "${profileInfo.surname} ${profileInfo.name} ${profileInfo.patronymic}"
            tvStaffPosition.text = profileInfo.functionality

            btnLogout.setOnClickListener {
                prefs.login = null
                prefs.password = null
                val action = ProfileDocumentsFragmentDirections.actionProfileDocumentsFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        }
    }



    companion object{
        const val SIGNED = "Подписанные"
        const val UNSIGNED = "Неподписанные"
    }
}