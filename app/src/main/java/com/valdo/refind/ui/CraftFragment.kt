package com.valdo.refind.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.valdo.refind.R
import com.valdo.refind.adapter.ListCraftAdapter
import com.valdo.refind.data.remote.ApiClient
import com.valdo.refind.data.remote.CraftResponse
import com.valdo.refind.data.remote.CraftsResponse
import com.valdo.refind.helper.BookmarkRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CraftFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListCraftAdapter
    private val TAG = "CraftFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_craft, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE

        // Initialize RecyclerView and adapter
        recyclerView = view.findViewById(R.id.recyclerViewCrafts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ListCraftAdapter(
            requireContext(),
            onItemClick = { craft -> openDetailCraftFragment(craft) },
            onBookmarkClick = { craft -> addToBookmarks(craft) }
        )
        recyclerView.adapter = adapter

        // Retrieve label from arguments
        val label = arguments?.getString("label") ?: ""
        fetchCrafts(label)
    }

    override fun onResume() {
        super.onResume()

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.GONE
    }

    private fun addToBookmarks(craft: CraftResponse) {
        BookmarkRepository.addCraftToBookmarks(
            craft,
            onSuccess = {
                // Show Snackbar with custom position
                val snackbar = Snackbar.make(
                    requireView(),
                    "${craft.Crafts?.name} added to bookmarks!",
                    Snackbar.LENGTH_SHORT
                )

                // Get the Snackbar's view
                val snackbarView = snackbar.view

                // Adjust the layout parameters to move it higher
                val params = snackbarView.layoutParams as FrameLayout.LayoutParams
                params.bottomMargin = 200  // Adjust this value to control how high the Snackbar appears
                snackbarView.layoutParams = params

                snackbar.show()
            },
            onFailure = { e ->
                Log.e("CraftFragment", "Error adding bookmark: ${e.message}", e)
                val snackbar = Snackbar.make(
                    requireView(),
                    "Error adding bookmark: ${e.message}",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
        )
    }

    private fun fetchCrafts(label: String) {
        // Now the full URL path will be handled in the API client
        val call = ApiClient.apiService.getCraftsByLabel(label)
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)

        progressBar?.visibility = View.VISIBLE

        Log.d(TAG, "Calling API for crafts with label: $label")

        call.enqueue(object : Callback<CraftsResponse> {
            override fun onResponse(call: Call<CraftsResponse>, response: Response<CraftsResponse>) {
                progressBar?.visibility = View.GONE

                Log.d("API Response", "Response code: ${response.code()}")
                Log.d("API Response", "Response body: ${response.body()}")
                Log.d("API Response", "Raw response: ${response.raw()}")

                if (response.isSuccessful) {
                    val craftsResponse = response.body()
                    if (craftsResponse != null && craftsResponse.status == "success") {
                        val resultList = craftsResponse.result
                        if (!resultList.isNullOrEmpty()) {
                            adapter.setCrafts(resultList) // Update RecyclerView with the crafts list
                        } else {
                            Log.e(TAG, "No crafts found in the response.")
                        }
                    } else {
                        Log.e(TAG, "Error: Invalid response status. Status: ${craftsResponse?.status}")
                    }
                } else {
                    Log.e(TAG, "API call failed with status: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CraftsResponse>, t: Throwable) {
                Log.e(TAG, "API request failed for label $label - Error: ${t.message}")
            }
        })
    }

    private fun openDetailCraftFragment(craft: CraftResponse) {
        Log.d("CraftFragment", "Opening DetailCraftFragment with craft: $craft")
        val fragment = DetailCraftFragment()
        val bundle = Bundle().apply {
            putInt("craft_id", craft.craft_id)
            putString("name", craft.Crafts?.name)
            putString("image", craft.Crafts?.image)
            putString("tools_materials", craft.Crafts?.tools_materials)
            putString("step", craft.Crafts?.step)
        }
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("DetailCraftFragment")
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.findViewById<View>(R.id.bottomAppBar)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}

