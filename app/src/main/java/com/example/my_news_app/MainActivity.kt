package com.example.my_news_app

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.my_news_app.databinding.ActivityMainBinding
import com.example.my_news_app.presentation.NewsViewModel
import com.example.my_news_app.presentation.fragments.DetailNewsFragment.Companion.DetailsNewsFragmentInstance
import com.example.my_news_app.presentation.fragments.MainNewsFragment
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
        MainNewsFragment().launchFragment()
        viewModel.showDetail.observe(this) { it ->
            if (it) {
                viewModel.articleUrl?.let { url ->
                    url.DetailsNewsFragmentInstance().launchFragment(addToBackStack = true)
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
        loadDialog.setCancelable(true)
        loadDialog.setContentView(R.layout.load_dialog)
        Glide.with(this).load(R.drawable.load_spin).into(loadDialog.findViewById(R.id.im_load))
    }

    private fun Fragment.launchFragment(addToBackStack:Boolean = false) {
        mBinding?.mainNavFragment?.let {
                val transaction = supportFragmentManager.beginTransaction()
                if(addToBackStack)transaction.addToBackStack("${this.javaClass.simpleName}")
                transaction.add(it.id, this@launchFragment)
                transaction.commit()
        }
    }
}