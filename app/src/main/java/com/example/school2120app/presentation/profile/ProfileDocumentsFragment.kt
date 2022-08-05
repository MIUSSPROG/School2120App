package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.school2120app.R
import com.example.school2120app.databinding.FragmentProfileDocumentsBinding
import com.example.school2120app.presentation.profile.adapter.ProfileViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ProfileDocumentsFragment: Fragment(R.layout.fragment_profile_documents) {

    private lateinit var binding: FragmentProfileDocumentsBinding
    private val fragTitles = listOf(
        "Подписанные",
        "Неподписанные"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileDocumentsBinding.bind(view)

        val fragList = listOf(DocumentSubscriptionFragment(fragTitles[0]), DocumentSubscriptionFragment(fragTitles[1]))
        val adapter = ProfileViewPagerAdapter(requireActivity(), fragList)
        binding.apply {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager){ tab, pos ->
                tab.text = fragTitles[pos]
            }.attach()
        }
    }
}