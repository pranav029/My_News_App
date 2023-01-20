package com.example.my_news_app

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.my_news_app.databinding.ActivityMainBinding
import com.example.my_news_app.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
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
                        bnvMain.isVisible = uiState.isBottomNavVisible
                    }
                }
        }
    }


    private fun showLoadDialog() = loadDialog.show()
    private fun hideLoadDialog() = loadDialog.dismiss()

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