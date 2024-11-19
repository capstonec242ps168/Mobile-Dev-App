package com.valdo.refind

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valdo.refind.databinding.FragmentBookmarkBinding
import kotlinx.coroutines.launch

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SettingPreferences
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity(), preferences)
    }
    private lateinit var bookmarkAdapter: ListEventAdapter
    private lateinit var bookmarkRv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        setupRecyclerView()
        observeFavoriteEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView() {
        bookmarkRv = binding.rvBookmark

        bookmarkAdapter = ListEventAdapter(
            onClickedItem = {
                val action = BookmarkFragmentDirections.actionBookmarkFragmentToDetailFragment(it.id)
                findNavController().navigate(action)
            },
            viewType = ListEventAdapter.BOOKMARK_VIEW_TYPE,
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner
        )

        bookmarkRv.apply {
            adapter = bookmarkAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeFavoriteEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookmarkEvents.collect { result ->
                    bookmarkAdapter.setBookmarkEvents(result)
                }
            }
        }
    }

}