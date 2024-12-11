package com.valdo.refind.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
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

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewBookmark)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = ListCraftAdapter(
            requireContext(),
            onItemClick = { craft, imageView, titleView -> openDetailCraftFragment(craft, imageView, titleView) },
            onBookmarkClick = { craft -> removeFromBookmarks(craft) }
        )

        recyclerView.adapter = adapter

        // Load the bookmarked items
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
        BookmarkRepository.toggleBookmark(
            craft,
            onSuccess = { isAdded ->
                if (!isAdded) {
                    val snackbar = Snackbar.make(
                        requireView(),
                        "${craft.Crafts?.name} dihapus dari bookmark!",
                        Snackbar.LENGTH_SHORT
                    )
                    adjustSnackbarPosition(snackbar)
                    snackbar.show()
                    loadBookmarks() // Refresh the list after removal
                }
            },
            onFailure = { e ->
                val snackbar = Snackbar.make(
                    requireView(),
                    "Terjadi kesalahan saat menghapus: ${e.message}",
                    Snackbar.LENGTH_SHORT
                )
                adjustSnackbarPosition(snackbar)
                snackbar.show()
            }
        )
    }

    private fun adjustSnackbarPosition(snackbar: Snackbar) {
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.bottomMargin = 200
        snackbarView.layoutParams = params
    }

    private fun openDetailCraftFragment(craft: CraftResponse, imageView: ImageView, titleView: TextView) {
        val fragment = DetailCraftFragment()
        val bundle = Bundle().apply {
            putInt("craft_id", craft.craft_id)
            putString("name", craft.Crafts?.name)
            putString("image", craft.Crafts?.image)
            putString("tools_materials", craft.Crafts?.tools_materials)
            putString("step", craft.Crafts?.step)
        }
        fragment.arguments = bundle

        // Set up transitions
        fragment.sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        fragment.enterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)

        parentFragmentManager.beginTransaction()
            .addSharedElement(imageView, imageView.transitionName)
            .addSharedElement(titleView, titleView.transitionName)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("DetailCraftFragment")
            .commit()
    }
}


