package com.example.my_news_app

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.my_news_app.databinding.ActivityMainBinding
import com.example.my_news_app.presentation.viewModels.MainViewModel
import com.example.my_news_app.presentation.viewModels.SearchViewModel
import com.example.my_news_app.utils.AnimationUtil.Companion.fadeInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideOutAnimation
import com.example.my_news_app.utils.UiHelper.Companion.hideKeyBoard
import com.example.my_news_app.utils.UiHelper.Companion.onTextChange
import com.example.my_news_app.utils.UiHelper.Companion.showKeyBoard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private var mBinding: ActivityMainBinding? = null
    private lateinit var loadDialog: Dialog

    @OptIn(FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)
        this.supportActionBar?.hide()
        initLoadDialog()
        mBinding?.run {
            btnSearch.setOnClickListener {
                appTitle.visibility = View.GONE
                btnSearch.visibility = View.GONE
                searchText.visibility = View.VISIBLE
                searchText.slideInAnimation(this@MainActivity)
                searchText.requestFocus()
                searchText.showKeyBoard(this@MainActivity)
                appBar.setExpanded(true)
                findNavController(mainNavFragment.id).navigate(R.id.action_HomeFragment_to_SearchNewsFragment)
                searchText.onTextChange()
                    .filter { it != null }.debounce(300).onEach {
                        searchViewModel.searchNews(it.toString())
                    }.launchIn(lifecycleScope)
            }
        }

        lifecycleScope.launch {
            mainViewModel.state.distinctUntilChanged { old, new -> old.equals(new) }
                .collectLatest { uiState ->
                    mBinding?.run {
                        appBar.setExpanded(uiState.isAppbarVisible, true)
                        if (uiState.isProgressDialogVisible) showLoadDialog()
                        else hideLoadDialog()
                    }
                }
        }
    }


    private fun showLoadDialog() = loadDialog.show()
    private fun hideLoadDialog() = loadDialog.dismiss()
    private fun closeSearchView() = mBinding?.run {
        searchText.slideOutAnimation(this@MainActivity)
        searchText.visibility = View.GONE
        appTitle.visibility = View.VISIBLE
        btnSearch.visibility = View.VISIBLE
        appTitle.fadeInAnimation(this@MainActivity)
        btnSearch.fadeInAnimation(this@MainActivity)
        searchText.hideKeyBoard(this@MainActivity)
        searchText.text.clear()
    }

    private fun initLoadDialog() {
        loadDialog = Dialog(this)
        loadDialog.setCancelable(false)
        loadDialog.setContentView(R.layout.load_dialog)
        Glide.with(this).load(R.drawable.load_spin).into(loadDialog.findViewById(R.id.im_load))
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        Glide.get(this).clearMemory()
        Glide.get(this).clearDiskCache()
    }
}