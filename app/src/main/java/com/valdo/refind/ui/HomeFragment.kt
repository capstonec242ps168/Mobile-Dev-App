package com.valdo.refind.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valdo.refind.R
import com.valdo.refind.data.remote.ApiClient
import com.valdo.refind.data.remote.ApiConfig
import com.valdo.refind.data.remote.NewsResponse
import com.valdo.refind.adapter.ListNewsAdapter
import com.valdo.refind.data.remote.NewsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class HomeFragment : Fragment() {

    private val baseUrl = ApiConfig.BASE_URL
    private lateinit var rvNews: RecyclerView
    private lateinit var listNewsAdapter: ListNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonClickListener(view, R.id.btnPlastik, "plastic")
        setButtonClickListener(view, R.id.btnKertas, "paper")
        setButtonClickListener(view, R.id.btnKardus, "cardboard")
        setButtonClickListener(view, R.id.btnBaju, "clothes")
        setButtonClickListener(view, R.id.btnKaleng, "metal")
        setButtonClickListener(view, R.id.btnSepatu, "shoes")
        setButtonClickListener(view, R.id.btnOrganik, "biological")
        setButtonClickListener(view, R.id.btnKaca, "white-glass")

        rvNews = view.findViewById(R.id.rvNews)
        setupRecyclerView()
        fetchNews()
    }

    private fun setButtonClickListener(view: View, buttonId: Int, endpoint: String) {
        view.findViewById<View>(buttonId).setOnClickListener {
            if (endpoint == "biological") {
                openNoCraftFragment(
                    R.drawable.gadogado_food,
                    getString(R.string.warning_bio),
                    getString(R.string.treatment_bio)
                )
            } else {
                logApiCall(endpoint)
                openCraftFragment(endpoint)
            }
        }
    }

    private fun openNoCraftFragment(imageResId: Int, warning: String, treatment: String) {
        val noCraftFragment = NoCraftFragment()
        val bundle = Bundle()
        bundle.putInt("imageResId", imageResId)
        bundle.putString("warning", warning)
        bundle.putString("treatment", treatment)
        bundle.putBoolean("isCraft", true)
        noCraftFragment.arguments = bundle

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Crafts"

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, noCraftFragment)
            .addToBackStack("NoCraftFragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun logApiCall(endpoint: String) {
        val url = "$baseUrl/$endpoint"
        Log.d("HomeFragment", "API called: $url")
    }

    private fun openCraftFragment(label: String) {
        val craftFragment = CraftFragment()
        val bundle = Bundle()
        bundle.putString("label", label)
        craftFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, craftFragment)
            .addToBackStack("CraftFragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        rvNews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        listNewsAdapter = ListNewsAdapter(emptyList()) { newsItem ->
            openDetailNewsFragment(newsItem)
        }
        rvNews.adapter = listNewsAdapter
    }
    private fun fetchNews() {
        val progressIndicator = view?.findViewById<ProgressBar>(R.id.progressIndicatorNews)
        progressIndicator?.visibility = View.VISIBLE

        val apiService = ApiClient.apiService
        apiService.getNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                progressIndicator?.visibility = View.GONE
                if (response.isSuccessful) {
                    val newsList = response.body()?.result ?: emptyList()
                    listNewsAdapter.updateNews(newsList)
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil berita", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
            }
        })
    }

    private fun openDetailNewsFragment(newsItem: NewsItem) {
        val detailFragment = DetailNewsFragment()
        val bundle = Bundle()
        bundle.putSerializable("newsItem", newsItem)
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack("DetailNewsFragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
