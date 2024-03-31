package com.devgary.contentviewdemo.screens.basic
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.devgary.contentcore.util.TAG
import com.devgary.contentcore.util.name
import com.devgary.contentviewdemo.R
import com.devgary.contentviewdemo.databinding.FragmentBasicBinding
import com.devgary.testcore.SampleContent

class BasicFragment : Fragment(), MenuProvider {
    private val basicViewModel: BasicViewModel by lazy {
        ViewModelProvider(this).get(BasicViewModel::class.java)
    }
    
    private var _binding: FragmentBasicBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView]
     */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasicBinding.inflate(inflater, container, false)
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
                basicViewModel.clearMemory()
                return true
            }
            else -> return false
        }.let { url ->
            basicViewModel.loadContent(url)
            return true
        }
    }
    
    private fun initViewModel() {
        basicViewModel.content.observe(viewLifecycleOwner) { 
            binding.contentview.showContent(it)
            binding.contentview.activate()
        }

        basicViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(
                /* context = */ context,
                /* text = */ "Error: $it",
                /* duration = */ Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.contentview.dispose()
        _binding = null
    }
}