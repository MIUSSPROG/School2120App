package com.example.school2120app.presentation.news

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource
import com.example.school2120app.databinding.FragmentNewsListBinding
import com.example.school2120app.presentation.news.adapter.NewsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NewsListFragment: Fragment(R.layout.fragment_news_list) {

    private lateinit var binding: FragmentNewsListBinding
    private val newsAdapter by lazy { NewsAdapter() }
    private val viewModel: NewsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsListBinding.bind(view)
        binding.apply {
            rvNewsList.adapter = newsAdapter
            viewModel.getNews()
            viewModel.newsListLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                        newsAdapter.submitList(it.data)
                        progressBarNews.visibility = View.INVISIBLE
                    }
                    is Resource.Loading -> {
                        progressBarNews.visibility = View.VISIBLE
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is NewsListViewModel.UIEvent.ShowSnackbar -> {
                            Snackbar.make(this@apply.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.getNews(query = s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

}