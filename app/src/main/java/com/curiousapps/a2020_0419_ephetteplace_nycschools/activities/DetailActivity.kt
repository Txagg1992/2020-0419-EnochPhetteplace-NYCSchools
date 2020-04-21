package com.curiousapps.a2020_0419_ephetteplace_nycschools.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.curiousapps.a2020_0419_ephetteplace_nycschools.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity() {

    private var detailExtras: Bundle? = null
    private val EXTRA_SCHOOL_DBN: String = "dbn"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getExtrasAndSetText()

    }

    private fun getExtrasAndSetText(){
        //Retrieve data from adapter to populate DetailsActivity
        detailExtras = intent.extras
        if (detailExtras != null){
            val schoolName = detailExtras!!.getString("schoolName")
            val schoolAddress = detailExtras!!.getString("schoolAddress")
            val schoolCity = detailExtras!!.getString("schoolCity")
            val schoolPhone = detailExtras!!.getString("schoolPhone")
            val schoolEmail = detailExtras!!.getString("schoolEmail")
            val schoolOverview = detailExtras!!.getString("schoolOverview")
            val schoolWebLink = detailExtras!!.getString("website")
            val schoolDbn = detailExtras!!.getString("dbn")

            //bind views, set values
            detail_school_name.text = schoolName
            detail_school_address.text = schoolAddress
            detail_school_city.text = schoolCity
            detail_school_phone.text = schoolPhone
            detail_school_overview.text = schoolOverview

            //Implicit intent to open mail provider on device
            detail_school_mail.setOnClickListener {
                val mailIntent = Intent(Intent.ACTION_SEND)
                mailIntent.type = "message/rfc822"
                mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(schoolEmail))//get from json
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "School Information")
                mailIntent.putExtra(Intent.EXTRA_TEXT, "")
                try {
                    startActivity(Intent.createChooser(mailIntent, "Send email..."))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(this, "There are no email clients installed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            //Intent to open nested SatActivity
            sat_button.setOnClickListener {
                val satIntent = Intent(this, SatActivity::class.java)
                satIntent.putExtra(EXTRA_SCHOOL_DBN, schoolDbn)
                startActivity(satIntent)
            }
            //Opens webView to display the school wep page
            detail_school_web.setOnClickListener {
                val webIntent = Intent(this, WebViewActivity::class.java)
                webIntent.putExtra("link", schoolWebLink)
                startActivity(webIntent)
            }

        }
        //Implicit intent to open devices phone dialer
        detail_school_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${detailExtras?.getString("schoolPhone")}")
            startActivity(intent)
        }
    }
}
