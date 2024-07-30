package ua.czrblz.vrg_soft_test_task

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.czrblz.data.utils.loadPictureThumbnail
import ua.czrblz.vrg_soft_test_task.adapter.PostsAdapter
import ua.czrblz.vrg_soft_test_task.databinding.ActivityMainBinding
import ua.czrblz.vrg_soft_test_task.listener.PictureListener

class MainActivity : AppCompatActivity(), PictureListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val postAdapter by lazy(LazyThreadSafetyMode.NONE) { PostsAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        lifecycleScope.launch {
            viewModel.data.collect {
                it?.let {
                    postAdapter.submitData(it)
                }
            }
        }
        lifecycleScope.launch {
            postAdapter.loadStateFlow.collect {
                val state = it.refresh
                binding.progress.isVisible = state is LoadState.Loading
            }
        }
    }

    override fun openPicture(imageUrl: String) {
        //todo
    }
}