package com.example.my_news_app

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.my_news_app.databinding.ActivityMainBinding
import com.example.my_news_app.presentation.viewModels.MainViewModel
import com.example.my_news_app.utils.AnimationUtil.Companion.fadeInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideOutAnimation
import com.example.my_news_app.utils.SearchEvent
import com.example.my_news_app.utils.UiHelper.Companion.hideKeyBoard
import com.example.my_news_app.utils.UiHelper.Companion.onTextChange
import com.example.my_news_app.utils.UiHelper.Companion.showKeyBoard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private var mBinding: ActivityMainBinding? = null
    private lateinit var loadDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)
        this.supportActionBar?.hide()
        initProgressDialog()
        mBinding?.run {
            supportFragmentManager.findFragmentById(R.id.main_nav_fragment)
                ?.let { bnvMain.setupWithNavController(it.findNavController()) }
        }
        lifecycleScope.subscribeUI()
    }

    private fun CoroutineScope.subscribeUI() {
        launch {
            mainViewModel.state
                .collect { uiState ->
                    mBinding?.run {
                        appBar.setExpanded(uiState.isAppbarVisible, true)
                        if (uiState.isProgressDialogVisible) showLoadDialog()
                        else hideLoadDialog()
                        if (uiState.isSearchGroupVisible) showSearchView()
                        else hideSearchView()
                        bnvMain.isVisible = uiState.isBottomNavVisible
                    }
                }
        }
        launch {
            mainViewModel.queryEvent.collectLatest {
                mBinding?.run {
                    when (it) {
                        is SearchEvent.onQueryResult -> {
                            pbSearch.isVisible = it.isSearchProgressVisible
                            ivClearText.isVisible = it.isCancelIconVisible
                        }
                        is SearchEvent.onProcessQuery -> {
                            pbSearch.isVisible = it.isSearchProgressVisible
                            ivClearText.isVisible = it.isCancelIconVisible
                        }
                        is SearchEvent.idle -> {
                            pbSearch.isVisible = it.isSearchProgressVisible
                            ivClearText.isVisible = it.isCancelIconVisible
                        }
                    }
                }
            }
        }
    }


    private fun showLoadDialog() = loadDialog.show()
    private fun hideLoadDialog() = loadDialog.dismiss()
    private fun hideSearchView() = mBinding?.run {
        etSearch.slideOutAnimation(this@MainActivity)
        gSearch.visibility = View.GONE
        appTitle.visibility = View.VISIBLE
        appTitle.fadeInAnimation(this@MainActivity)
        etSearch.hideKeyBoard(this@MainActivity)
        etSearch.text?.clear()
    }

    private fun showSearchView() = mBinding?.run {
        appTitle.visibility = View.GONE
        gSearch.isVisible = true
        pbSearch.isVisible = false
        ivClearText.isVisible = false
        etSearch.slideInAnimation(this@MainActivity)
        etSearch.requestFocus()
        etSearch.showKeyBoard(this@MainActivity)
        appBar.setExpanded(true)
        etSearch.onTextChange()
            .filter { it != null }.debounce(300).onEach {
                mainViewModel.handleTextUpdate(it.toString())
            }.launchIn(lifecycleScope)
        ivClearText.setOnClickListener {
            etSearch.text?.clear()
            mainViewModel.handleTextUpdate(etSearch.text.toString())
        }
    }

    private fun initProgressDialog() {
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