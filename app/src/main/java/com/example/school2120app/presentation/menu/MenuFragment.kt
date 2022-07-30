package com.example.school2120app.presentation.menu

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentMenuBinding
import com.example.school2120app.presentation.menu.adapter.MenuAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding
    private val viewModel: MenuViewModel by viewModels()
    private val adapter by lazy { MenuAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)
        binding.apply {

            rvMenuItems.adapter = adapter
            viewModel.getMenus()
            viewModel.menusLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Success -> {
                        pbLoadMenu.visibility = INVISIBLE
                        adapter.submitList(it.data)
                    }
                    is Loading -> {
                        pbLoadMenu.visibility = VISIBLE
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            pbLoadMenu.visibility = INVISIBLE
                            Snackbar.make(this@apply.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }
}