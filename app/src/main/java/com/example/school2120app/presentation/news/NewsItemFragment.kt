package com.example.school2120app.presentation.news

import android.os.Bundle
import android.text.Html
import android.transition.ChangeBounds
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.school2120app.R
import com.example.school2120app.core.util.PicassoImageGetter
import com.example.school2120app.databinding.FragmentNewsItemBinding
import com.example.school2120app.domain.model.news.News
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsItemFragment: Fragment(R.layout.fragment_news_item) {

    private lateinit var binding: FragmentNewsItemBinding
    private val args: NewsItemFragmentArgs by navArgs()
    private var news: News? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewsItemBinding.bind(view)
        news = args.news
        sharedElementEnterTransition = ChangeBounds()
        binding.apply {

            val publishDateText = news?.publishDate?.split('T')?.get(0) ?: ""
            val htmlContent = Html.fromHtml(news?.content, Html.FROM_HTML_MODE_COMPACT, PicassoImageGetter(tvNewsItemDetailContent, requireContext()), null)

            tvNewsItemDetailHeader.text = news?.name
            tvNewsItemDetailPublishDate.text = "$publishDateText "
            tvNewsItemDetailContent.text = htmlContent
        }

    }
}