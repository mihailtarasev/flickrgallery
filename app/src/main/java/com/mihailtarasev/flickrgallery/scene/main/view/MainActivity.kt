package com.mihailtarasev.flickrgallery.scene.main.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mihailtarasev.flickrgallery.R
import com.mihailtarasev.flickrgallery.databinding.ActivityMainBinding
import com.mihailtarasev.flickrgallery.extension.WrapContentLinearLayoutManager
import com.mihailtarasev.flickrgallery.extension.displayActivity
import com.mihailtarasev.flickrgallery.scene.main.model.MainUseCasePhotoModel
import com.mihailtarasev.flickrgallery.scene.main.router.MainRouter
import com.mihailtarasev.flickrgallery.scene.main.usecase.MainViewModel

class MainActivity : AppCompatActivity(), MainActivityAdapterCallback {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var router: MainRouter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityAdapter: MainActivityAdapter
    private var oldSizePhotoList = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
        setupViews()
        setupViewModelObserver()
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
        binding.searchView.setOnQueryTextFocusChangeListener { _ , hasFocus ->
            if (!hasFocus) {
                viewModel.updateNotFilteredClearedPage()
            }
        }

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
        val rotation = this.displayActivity().rotation
        binding.recyclerview.layoutManager = WrapContentLinearLayoutManager(applicationContext, rotation, portraitCountRows, landscapeCountRows)
        mainActivityAdapter = MainActivityAdapter(this, applicationContext)
        binding.recyclerview.adapter = mainActivityAdapter
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.recyclerview.canScrollVertically(1)) {
                    viewModel.uploadNextAvailablePage()
                }
            }
        })
        viewModel.uploadFirstPage()
    }

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

    private fun updateListAdapter(photoList: ArrayList<MainUseCasePhotoModel>) {
        mainActivityAdapter.updateList(binding.recyclerview, photoList, oldSizePhotoList, photoList.size)
        oldSizePhotoList = photoList.size
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