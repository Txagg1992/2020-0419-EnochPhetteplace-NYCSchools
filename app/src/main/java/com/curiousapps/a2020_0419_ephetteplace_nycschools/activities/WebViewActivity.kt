package com.curiousapps.a2020_0419_ephetteplace_nycschools.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import com.curiousapps.a2020_0419_ephetteplace_nycschools.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val myWebView = WebView(this@WebViewActivity)
        setContentView(myWebView)
        //myWebView.loadUrl("https://www.bkmusicntheatre.com/")

        val extra  = intent.extras
        if (extra != null){
            val link = extra.get("link")
            myWebView.loadUrl("https://${link.toString()}")
        }
        Log.d("WebView", "Page Loaded")
        Log.d("%%%URL%%%", extra.toString())
    }
}
