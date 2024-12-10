package com.valdo.refind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.valdo.refind.R
import com.valdo.refind.data.remote.NewsItem

class DetailNewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val newsItem = arguments?.getSerializable("newsItem") as? NewsItem

        if (newsItem != null) {
            view.findViewById<TextView>(R.id.news_title).text = newsItem.title
            view.findViewById<TextView>(R.id.news_content).text = newsItem.content
            Glide.with(this).load(newsItem.image)
                .into(view.findViewById(R.id.news_image))
        }

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}