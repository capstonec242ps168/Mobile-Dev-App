package com.valdo.refind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                Toast.makeText(requireContext(), "Error loading bookmarks: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun removeFromBookmarks(craft: CraftResponse) {
        BookmarkRepository.removeCraftFromBookmarks(
            craft,
            onSuccess = {
                Toast.makeText(requireContext(), "${craft.Crafts?.name} removed from bookmarks!", Toast.LENGTH_SHORT).show()
                loadBookmarks() // Refresh the bookmark list
            },
            onFailure = { e ->
                Toast.makeText(requireContext(), "Error removing bookmark: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
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

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        activity?.findViewById<View>(R.id.fab)?.visibility = View.VISIBLE
    }
}
