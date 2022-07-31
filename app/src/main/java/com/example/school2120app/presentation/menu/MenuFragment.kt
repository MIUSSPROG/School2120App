package com.example.school2120app.presentation.menu

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.school2120app.R
import com.example.school2120app.core.util.ActionListener
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentMenuBinding
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.presentation.menu.adapter.MenuAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding
    private val viewModel: MenuViewModel by viewModels()
    private val adapter by lazy { MenuAdapter(object : ActionListener<MenuItem> {
        override fun onItemClicked(item: MenuItem, view: View?) {
            val action = MenuFragmentDirections.actionMenuFragmentToMenuDetailFragment(item)
            findNavController().navigate(action)
        }
    })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)
        binding.apply {

            swipeRefreshLayoutMenu.setOnRefreshListener {
                viewModel.getMenus(fetchFromRemote = true)
                swipeRefreshLayoutMenu.isRefreshing = false
            }

            rvMenuItems.adapter = adapter
            viewModel.getMenus(fetchFromRemote = false)
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