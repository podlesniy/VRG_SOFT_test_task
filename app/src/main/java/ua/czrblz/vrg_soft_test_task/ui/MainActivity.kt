package ua.czrblz.vrg_soft_test_task.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.czrblz.vrg_soft_test_task.MainViewModel
import ua.czrblz.vrg_soft_test_task.adapter.PostsAdapter
import ua.czrblz.vrg_soft_test_task.databinding.ActivityMainBinding
import ua.czrblz.vrg_soft_test_task.listener.OpenPictureListener
import ua.czrblz.vrg_soft_test_task.listener.SavePictureListener
import ua.czrblz.vrg_soft_test_task.utils.hasNetworkConnection

class MainActivity : AppCompatActivity(), OpenPictureListener, SavePictureListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val postAdapter by lazy(LazyThreadSafetyMode.NONE) { PostsAdapter(this, this) }
    lateinit var currentLayoutManager: LinearLayoutManager

    private val internetConnectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (context.hasNetworkConnection())
                postAdapter.retry()
        }
    }

    companion object {
        const val REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK
        currentLayoutManager = LinearLayoutManager(this@MainActivity)
        with(binding) {
            rvPosts.apply {
                adapter = postAdapter
                layoutManager = currentLayoutManager
                addOnScrollListener(object: RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        scrollToStart.apply {
                            isVisible = currentLayoutManager.findLastVisibleItemPosition() > 15
                            setOnClickListener {
                                rvPosts.smoothScrollToPosition(0)
                            }
                        }
                    }
                })
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
                    progress.isVisible = state is LoadState.Loading
                    emptyPlaceholder.isVisible =
                        state is LoadState.Error && postAdapter.itemCount == 0
                }
            }

            swipeLayout.setOnRefreshListener {
                swipeLayout.isRefreshing = false
                postAdapter.refresh()
            }
        }
        registerReceiver(internetConnectionReceiver, IntentFilter(CONNECTIVITY_ACTION))
    }

    override fun openPicture(imageUrl: String, thumbnailUrl: String) {
        PictureDialogFragment.newInstance(imageUrl, thumbnailUrl).show(supportFragmentManager, null)
    }

    override fun savePicture(imageUrl: String) {
        viewModel.updateImageUrl(imageUrl)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        } else {
            savePictureIfGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && !grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                    savePictureIfGranted()
                } else {
                    Toast.makeText(this, "You need to grant permissions to the application!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun savePictureIfGranted() {
        viewModel.currentImageUrl.value?.let { imageUrl ->
            viewModel.savePicture(imageUrl, { imageName ->
                Toast.makeText(this, "$imageName is downloaded", Toast.LENGTH_SHORT).show()
            }) {
                Toast.makeText(this, "Something went wrong while loading, try again later!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        registerReceiver(internetConnectionReceiver, IntentFilter(CONNECTIVITY_ACTION))
    }
}