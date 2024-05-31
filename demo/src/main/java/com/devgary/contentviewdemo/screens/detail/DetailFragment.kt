package com.devgary.contentviewdemo.screens.detail
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentcore.util.visibleOrGone
import com.devgary.contentviewdemo.R
import com.devgary.contentviewdemo.databinding.FragmentDetailBinding
import com.devgary.testcore.SampleContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(), MenuProvider {
    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }
    
    private var _binding: FragmentDetailBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView]
     */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initViewModel()
    }
    
    private fun initMenu() {
        (requireActivity() as? MenuHost)?.let {
            it.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        } ?: run {
            Log.e(TAG, "Could not cast activity to ${name<MenuHost>()}")
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_demo, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.menu_image -> SampleContent.IMAGE_CONTENT
            R.id.menu_image_no_ext -> SampleContent.IMAGE_CONTENT_NO_EXTENSION
            R.id.menu_gif -> SampleContent.GIF_CONTENT
            R.id.menu_video -> SampleContent.MP4_VIDEO_CONTENT
            R.id.menu_streamable -> SampleContent.STREAMABLE.BASIC_URL
            R.id.menu_streamable_parse_webpage -> SampleContent.STREAMABLE.HLS_URL
            R.id.menu_gfycat_video -> SampleContent.GFYCAT_URL
            R.id.menu_imgur_album -> SampleContent.IMGUR_ALBUM_GALLERY_URL
            R.id.menu_clear_memory -> {
                detailViewModel.clearMemory()
                return true
            }
            R.id.menu_cancel_load -> {
                detailViewModel.cancelLoad()
                return true
            }
            else -> return false
        }.let { url ->
            detailViewModel.loadContent(url)
            return true
        }
    }
    
    private fun initViewModel() {
        detailViewModel.detailDataState.observe(viewLifecycleOwner) {
            if (it.loadingVisibility) {
                binding.loadingIndicator.show()
            } else {
                if (it.content == null) {
                    binding.loadingIndicator.hide()
                } else {
                    // TODO: If we are showing content, we want to hide the loading
                    // indicator only after the content is done loading/rendering.
                    // However, currently ContentView does not report that information
                    // so just dont hide the loading indicator and when the content is
                    // rendered, it will cover the indicator (if content is large enough)
                }
            }

            binding.contentview.visibleOrGone(it.contentVisibility)
            it.content?.let { content ->
                binding.contentview.showContent(content)
                binding.contentview.activate()
            } ?: run { 
                binding.contentview.deactivate()
            }

            binding.errorView.visibleOrGone(it.errorVisibility)
            binding.errorTextView.text = it.errorText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.contentview.dispose()
        _binding = null
    }
}