package com.valdo.refind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.valdo.refind.R
import com.valdo.refind.adapter.ListCraftAdapter
import com.valdo.refind.data.remote.CraftResponse
import com.valdo.refind.helper.BookmarkRepository

class BookmarkFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListCraftAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        recyclerView = view.findViewById(R.id.recyclerViewBookmark)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ListCraftAdapter(
            requireContext(),
            onItemClick = { craft -> openDetailCraftFragment(craft) },
            onBookmarkClick = { craft -> removeFromBookmarks(craft) }
        )

        recyclerView.adapter = adapter

        loadBookmarks()
    }

    private fun loadBookmarks() {
        BookmarkRepository.getBookmarkedCrafts(
            onResult = { bookmarks ->
                adapter.setCrafts(bookmarks)
            },
            onFailure = { e ->
                val snackbar = Snackbar.make(
                    requireView(),
                    "Error loading bookmarks: ${e.message}",
                    Snackbar.LENGTH_SHORT
                )
                // Adjust position for the error snackbar
                adjustSnackbarPosition(snackbar)
                snackbar.show()
            }
        )
    }

    private fun removeFromBookmarks(craft: CraftResponse) {
        BookmarkRepository.removeCraftFromBookmarks(
            craft,
            onSuccess = {
                val snackbar = Snackbar.make(
                    requireView(),
                    "${craft.Crafts?.name} removed from bookmarks!",
                    Snackbar.LENGTH_SHORT
                )
                // Adjust position for the success snackbar
                adjustSnackbarPosition(snackbar)
                snackbar.show()
                loadBookmarks() // Refresh the bookmark list
            },
            onFailure = { e ->
                val snackbar = Snackbar.make(
                    requireView(),
                    "Error removing bookmark: ${e.message}",
                    Snackbar.LENGTH_SHORT
                )
                // Adjust position for the error snackbar
                adjustSnackbarPosition(snackbar)
                snackbar.show()
            }
        )
    }

    private fun adjustSnackbarPosition(snackbar: Snackbar) {
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.bottomMargin = 200 // Adjust this value to control how high the Snackbar appears
        snackbarView.layoutParams = params
    }

    private fun openDetailCraftFragment(craft: CraftResponse) {
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
}

