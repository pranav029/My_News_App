package com.example.my_news_app

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import com.example.my_news_app.storage.Article

class FullNews : AppCompatActivity() {
    private lateinit var  pDialogue:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_news)
        this.supportActionBar?.hide()
        pDialogue= ProgressDialog(this)
        pDialogue.setMessage("Loading Content")
        pDialogue.volumeControlStream=ProgressDialog.STYLE_SPINNER
        pDialogue.isIndeterminate=true
        pDialogue.show()
        val bundle: Bundle = intent.extras!!
        val url= bundle?.get("URL")
        var web:WebView=findViewById(R.id.webview)
        web.webViewClient=webClient()
        web.loadUrl(url.toString())
    }
    inner class webClient:WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!)
            return false;
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
//           Toast.makeText(this@FullNews,"Success",Toast.LENGTH_LONG).show()
            pDialogue.isIndeterminate=false
            pDialogue.hide()
        }
    }

}