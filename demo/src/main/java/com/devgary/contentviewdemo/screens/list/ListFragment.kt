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
import com.devgary.contentcore.util.TAG
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentviewdemo.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    @Inject lateinit var contentLinkHandler: ContentLinkHandler

    private val listViewModel: ListViewModel by viewModels()

    private var binding: FragmentListBinding? = null
    private var adapter: ContentAdapter? = null

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
            }
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