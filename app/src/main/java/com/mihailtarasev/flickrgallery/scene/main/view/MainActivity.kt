package com.mihailtarasev.flickrgallery.scene.main.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mihailtarasev.flickrgallery.R
import com.mihailtarasev.flickrgallery.databinding.ActivityMainBinding
import com.mihailtarasev.flickrgallery.extension.WrapContentLinearLayoutManager
import com.mihailtarasev.flickrgallery.extension.displayActivity
import com.mihailtarasev.flickrgallery.scene.main.model.MainPhotoModel
import com.mihailtarasev.flickrgallery.scene.main.router.MainRouter
import com.mihailtarasev.flickrgallery.scene.main.usecase.MainViewModel

class MainActivity : AppCompatActivity(), MainActivityAdapterCallback {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var router: MainRouter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityAdapter: MainActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
        setupViews()
        setupViewModelObserver()
        uploadFirstPage()
    }

    private fun configure() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        router = MainRouter(this)
    }

    private fun setupViews() {
        setupSearchView()
        setupRecyclerview()
        setupRefreshControl()
    }

    private fun setupSearchView() {
        setupSearchViewFocusChangeListener()
        setupSearchViewSetTextListener()
    }

    private fun setupSearchViewFocusChangeListener() {
        binding.searchView.setOnQueryTextFocusChangeListener { _ , hasFocus ->
            if (!hasFocus) {
                viewModel.updateNotFilteredClearedPage()
            }
        }
    }

    private fun setupSearchViewSetTextListener() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.updateClearedPage()
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.setFilterWithDefault(p0)
                return false
            }
        })
    }

    private fun setupRefreshControl() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.updateClearedPage()
        }
    }

    private fun setupRecyclerview() {
        binding.recyclerview.setHasFixedSize(true)
        setupRecyclerviewAdapter()
        setupRecyclerviewLayoutManager()
        setupRecyclerviewOnScrollListener()
    }

    private fun setupRecyclerviewAdapter() {
        val placeholder = ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)!!
        mainActivityAdapter = MainActivityAdapter(this, placeholder)
        binding.recyclerview.adapter = mainActivityAdapter
    }

    private fun setupRecyclerviewLayoutManager() {
        val rotation = this.displayActivity().rotation
        binding.recyclerview.layoutManager = WrapContentLinearLayoutManager(this, rotation, portraitCountRows, landscapeCountRows)
    }

    private fun setupRecyclerviewOnScrollListener() {
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.recyclerview.canScrollVertically(1)) {
                    uploadNextAvailablePage()
                }
            }
        })
    }

    private fun uploadNextAvailablePage() = viewModel.uploadNextAvailablePage()

    private fun setupViewModelObserver() {
        viewModel.pageClearedLiveData.observe(this) {
            clearListAdapter()
        }

        viewModel.photoListUpdatedLiveData.observe(this) {
            updateListAdapter(it)
        }

        viewModel.isRefreshingLiveData.observe(this) {
            binding.defaultProgress.isVisible = it
        }

        viewModel.isAlertFiredLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadFirstPage() = viewModel.uploadFirstPage()

    private fun updateListAdapter(photoList: ArrayList<MainPhotoModel>) {
        mainActivityAdapter.updateList(binding.recyclerview, photoList)
    }

    private fun clearListAdapter() {
        mainActivityAdapter.clearList()
    }

    override fun onMainActivityAdapterItemCLick(imageUrl: String) {
        router.startDetailsActivity(imageUrl)
    }

    companion object {
        const val portraitCountRows = 3
        const val landscapeCountRows = 4
    }
}