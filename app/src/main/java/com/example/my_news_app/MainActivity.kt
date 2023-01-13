package com.example.my_news_app

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.my_news_app.databinding.ActivityMainBinding
import com.example.my_news_app.presentation.fragments.DetailNewsFragment.Companion.DetailsNewsFragmentInstance
import com.example.my_news_app.presentation.fragments.MainNewsFragment
import com.example.my_news_app.presentation.fragments.SearchNewsFragment
import com.example.my_news_app.presentation.uistate.MainActivityState
import com.example.my_news_app.presentation.viewModels.MainViewModel
import com.example.my_news_app.presentation.viewModels.SearchViewModel
import com.example.my_news_app.utils.AnimationUtil.Companion.fadeInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideOutAnimation
import com.example.my_news_app.utils.UiHelper.Companion.hideKeyBoard
import com.example.my_news_app.utils.UiHelper.Companion.launchFragment
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
            MainNewsFragment().launchFragment(mainNavFragment, supportFragmentManager)
            btnSearch.setOnClickListener {
                appTitle.visibility = View.GONE
                btnSearch.visibility = View.GONE
                searchText.visibility = View.VISIBLE
                searchText.slideInAnimation(this@MainActivity)
                searchText.requestFocus()
                searchText.showKeyBoard(this@MainActivity)
                appBar.setExpanded(true)
                SearchNewsFragment().launchFragment(
                    mainNavFragment,
                    supportFragmentManager,
                    true
                )
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
                        appBar.isVisible = uiState.isAppbarVisible
                        if (uiState.isProgressDialogVisible) showLoadDialog()
                        else hideLoadDialog()
                        handleClick(uiState)
                    }
                }
        }
    }

    private fun handleClick(uiState: MainActivityState) {
        if (uiState.articleClicked) {
            mBinding?.run {
                uiState.articleUrl?.DetailsNewsFragmentInstance()
                    ?.launchFragment(mainNavFragment, supportFragmentManager, true)
                appBar.setExpanded(true)
                closeSearchView()
            }
            mainViewModel.clickActionFinished()
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
}