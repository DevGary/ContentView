package com.devgary.contentviewdemo.screens.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devgary.contentcore.util.TAG
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentviewdemo.util.RecyclerViewUtils
import com.devgary.contentviewdemo.R
import com.devgary.contentviewdemo.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    @Inject lateinit var contentLinkHandler: ContentLinkHandler

    private val listViewModel: ListViewModel by viewModels()

    private var binding: FragmentListBinding? = null
    private var adapter: ContentAdapter? = null
    
    private var lastPlayedPosition = -1

    private val recyclerViewOnScrolled =
        MutableSharedFlow<Int>(
            replay = 0, 
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        binding?.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ContentAdapter(
                    context = context,
                    contentLinkHandler = contentLinkHandler
                ).also { adapter ->
                    listViewModel.urls.value?.let {
                        adapter.setData(it)
                    }
                }
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        recyclerViewOnScrolled.tryEmit(dy)
                    }
                })
            }

            recyclerViewOnScrolled.debounce(500).onEach {
                var lastFocusedPositionCalculationResult = RecyclerViewUtils.calculateFocusedRecyclerViewItemPosition(
                    recyclerView, R.id.contentview
                )

                if (lastFocusedPositionCalculationResult == lastPlayedPosition) return@onEach
                
                val viewHolderToPlay: RecyclerView.ViewHolder? =
                    recyclerView.findViewHolderForAdapterPosition(
                        lastFocusedPositionCalculationResult
                    )

                (viewHolderToPlay as? ContentAdapter.ContentViewHolder)?.let {
                    lastPlayedPosition = lastFocusedPositionCalculationResult
                    it.showContent()
                }
            }.launchIn(MainScope())
        } ?: run {
            Log.e(TAG, "Trying to access ViewBinding that is null")
        }
    }

    private fun initViewModel() {
        listViewModel.urls.observe(viewLifecycleOwner) {
            adapter?.setData(it)
        }

        listViewModel.error.observe(viewLifecycleOwner) {
            showErrorMessage(it)
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(
            /* context = */ context,
            /* text = */ "Error: $message",
            /* duration = */ Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}