package ua.czrblz.vrg_soft_test_task.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.czrblz.vrg_soft_test_task.MainViewModel
import ua.czrblz.vrg_soft_test_task.R
import ua.czrblz.vrg_soft_test_task.adapter.PostsAdapter
import ua.czrblz.vrg_soft_test_task.databinding.ActivityMainBinding
import ua.czrblz.vrg_soft_test_task.listener.OpenPictureListener
import ua.czrblz.vrg_soft_test_task.listener.SavePictureListener
import ua.czrblz.vrg_soft_test_task.utils.hasNetworkConnection
import ua.czrblz.vrg_soft_test_task.utils.toast

private const val SCROLL_UP_BUTTON_VISIBLE_ON_ITEM = 15
class MainActivity : AppCompatActivity(), OpenPictureListener, SavePictureListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val postAdapter by lazy(LazyThreadSafetyMode.NONE) { PostsAdapter(this, this) }
    lateinit var currentLayoutManager: LinearLayoutManager

    private val internetConnectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (context.hasNetworkConnection())
                postAdapter.retry()
            else
                toast(R.string.internet_error)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var numOfGrantedPermissions = 0
        permissions.entries.forEach {
            if (it.key in permissions && it.value) {
                numOfGrantedPermissions++
            }
        }
        if (numOfGrantedPermissions == permissions.size) {
            savePictureIfGranted()
        } else {
            toast(R.string.permissions_error)
        }
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
                            isVisible = currentLayoutManager.findLastVisibleItemPosition() > SCROLL_UP_BUTTON_VISIBLE_ON_ITEM
                            setOnClickListener {
                                rvPosts.smoothScrollToPosition(0)
                            }
                        }
                    }
                })
            }

            lifecycleScope.launch {
                viewModel.data.collect { pagingData ->
                    pagingData?.let { data ->
                        postAdapter.submitData(data)
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
        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requestMediaPermissions()
        else
            requestLegacyPermissions()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestMediaPermissions() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                savePictureIfGranted()
            }
            else -> requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            )
        }
    }

    private fun requestLegacyPermissions() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                savePictureIfGranted()
            }
            else -> requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun savePictureIfGranted() {
        viewModel.currentImageUrl.value?.let { imageUrl ->
            viewModel.savePicture(imageUrl, { imageName ->
                toast(getString(R.string.downloaded_file, imageName))
            }) {
                toast(R.string.something_error)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetConnectionReceiver)
    }
}