package com.example.my_news_app

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.my_news_app.databinding.ActivityMainBinding
import com.example.my_news_app.presentation.NewsViewModel
import com.example.my_news_app.presentation.fragments.DetailNewsFragment.Companion.DetailsNewsFragmentInstance
import com.example.my_news_app.presentation.fragments.MainNewsFragment
import com.example.my_news_app.presentation.fragments.SearchNewsFragment
import com.example.my_news_app.utils.UiHelper.Companion.hideKeyBoard
import com.example.my_news_app.utils.UiHelper.Companion.launchFragment
import com.example.my_news_app.utils.UiHelper.Companion.showKeyBoard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: NewsViewModel by viewModels()
    private var mBinding: ActivityMainBinding? = null
    private lateinit var loadDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)
        this.supportActionBar?.hide()
        initLoadDialog()
        mBinding?.let {
            with(it) {
                MainNewsFragment().launchFragment(mainNavFragment, supportFragmentManager)
                btnSearch.setOnClickListener {
                    appTitle.visibility = View.GONE
                    btnSearch.visibility = View.GONE
                    searchText.visibility = View.VISIBLE
                    SearchNewsFragment().launchFragment(
                        mainNavFragment,
                        supportFragmentManager,
                        true
                    )
                    searchText.requestFocus()
                    searchText.showKeyBoard(this@MainActivity)
                    appBar.setExpanded(true)
                }
                searchText.setOnClickListener {
                    appTitle.visibility = View.VISIBLE
                    btnSearch.visibility = View.VISIBLE
                    searchText.visibility = View.GONE
                    searchText.hideKeyBoard(this@MainActivity)
                }
            }
        }
        viewModel.showDetail.observe(this) { show ->
            if (show) {
                viewModel.articleUrl?.let { url ->
                    mBinding?.let { it ->
                        with(it) {
                            mainNavFragment.let {
                                url.DetailsNewsFragmentInstance()
                                    .launchFragment(it, supportFragmentManager, true)
                            }
                            appBar.setExpanded(true)
                        }
                    }
                }
                viewModel.disableShowDetailsFragment()
            }
        }
        viewModel.progressDialogVisible.observe(this) {
            if (it) showLoadDialog()
            else hideLoadDialog()
        }
    }


    private fun showLoadDialog() = loadDialog.show()
    private fun hideLoadDialog() = loadDialog.dismiss()

    private fun initLoadDialog() {
        loadDialog = Dialog(this)
        loadDialog.setCancelable(false)
        loadDialog.setContentView(R.layout.load_dialog)
        Glide.with(this).load(R.drawable.load_spin).into(loadDialog.findViewById(R.id.im_load))
    }

}