package com.devgary.contentviewdemo.screens.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.removeFromParentView
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
class ListFragment : Fragment(), MenuProvider {
    @Inject lateinit var contentLinkHandler: ContentLinkHandler

    private val listViewModel: ListViewModel by viewModels()

    private var binding: FragmentListBinding? = null
    private var adapter: ContentAdapter? = null
    private var layoutManager: LinearLayoutManager? = null    
    
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
        initMenu()
    }
    
    private fun initMenu() {
        (requireActivity() as? MenuHost)?.let {
            it.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        } ?: run {
            Log.e(TAG, "Could not cast activity to ${name<MenuHost>()}")
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.menu_clear_memory -> {
                binding?.recyclerView?.let {
                    RecyclerViewUtils.getAllVisibleViewHolders(it).onEach { viewHolder ->
                        (viewHolder as? ContentAdapter.ContentViewHolder)?.let { contentViewHolder ->
                            with(contentViewHolder.binding.contentview) {
                                dispose()
                                removeFromParentView()
                            }
                        }
                    }
                }

                true
            }
            else -> false
        }
    }

    private fun initViews() {
        binding?.apply {
            recyclerView.apply {
                this@ListFragment.layoutManager = LinearLayoutManager(context)
                this@ListFragment.adapter = ContentAdapter(
                    context = context,
                    contentLinkHandler = contentLinkHandler
                ).also { adapter ->
                    listViewModel.urls.value?.let {
                        adapter.setData(it)
                    }
                }

                layoutManager = this@ListFragment.layoutManager
                adapter = this@ListFragment.adapter
                
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        recyclerViewOnScrolled.tryEmit(dy)
                    }
                })
            }

            recyclerViewOnScrolled.debounce(250).onEach {
                val visibleItemPositions = RecyclerViewUtils.getVisibleItemPositions(layoutManager)
                val positionsToPlay = mutableListOf<Int>()
                if (visibleItemPositions.size > 2) {
                    positionsToPlay.addAll(visibleItemPositions.subList(visibleItemPositions.size - 2 - 1, visibleItemPositions.size - 1))
                }
                
                positionsToPlay.onEach { position ->
                    val viewHolderToPlay = recyclerView.findViewHolderForAdapterPosition(position)
                    (viewHolderToPlay as? ContentAdapter.ContentViewHolder)?.let {
                        Log.d(TAG, "Attempting to play content at position $position")
                        it.showContent()
                    }
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