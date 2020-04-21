package com.curiousapps.a2020_0419_ephetteplace_nycschools.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.curiousapps.a2020_0419_ephetteplace_nycschools.R
import com.curiousapps.a2020_0419_ephetteplace_nycschools.activities.DetailActivity
import com.curiousapps.a2020_0419_ephetteplace_nycschools.activities.WebViewActivity
import com.curiousapps.a2020_0419_ephetteplace_nycschools.model.Schools

class SchoolListAdapter(
    private val list: ArrayList<Schools>,
    private val context: Context) : RecyclerView.Adapter<SchoolListAdapter.ViewHolder>() {

    private val EXTRA_SCHOOL_NAME: String = "schoolName"
    private val EXTRA_SCHOOL_ADDRESS: String = "schoolAddress"
    private val EXTRA_SCHOOL_CITY: String = "schoolCity"
    private val EXTRA_SCHOOL_PHONE: String = "schoolPhone"
    private val EXTRA_SCHOOL_EMAIL: String = "schoolEmail"
    private val EXTRA_SCHOOL_OVERVIEW: String = "schoolOverview"
    private val EXTRA_SCHOOL_DBN: String = "dbn"
    private val EXTRA_SCHOOL_WEBSITE: String = "website"

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
       val view = LayoutInflater.from(context)
           .inflate(R.layout.school_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }
// inner class needed to persist context without creating a new instance of context twice
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //bind views to a reference
        var schoolName: TextView = itemView.findViewById<TextView>(R.id.text_view_school_name)
        var schoolAddress: TextView = itemView.findViewById<TextView>(R.id.text_view_school_address)
        var schoolCity: TextView = itemView.findViewById<TextView>(R.id.text_view_school_city)
        var schoolState: TextView = itemView.findViewById<TextView>(R.id.text_view_school_state)
        var schoolZip: TextView = itemView.findViewById<TextView>(R.id.text_view_school_zip)
        var schoolPhone: TextView = itemView.findViewById<TextView>(R.id.text_view_school_phone)
        var schoolWebLink: ImageView = itemView.findViewById<ImageView>(R.id.icon_web_link)


        fun bindView(schools: Schools){
            //bind data to views
            schoolName.text = schools.mSchoolName
            schoolAddress.text = schools.mPrimaryAddressLine1
            schoolCity.text = schools.mCity
            schoolState.text = schools.mStateCode
            schoolZip.text = schools.mZip
            schoolPhone.text = schools.mPhoneNumber

            // opens WebView for the school's web page
            schoolWebLink.setOnClickListener {
                val webIntent = Intent(context, WebViewActivity::class.java)
                webIntent.putExtra("link", schools.mWebsite.toString())
                context.startActivity(webIntent)
            }
            //itemView to make the cardView a clickable area at a specific position in the array/recyclerView
            itemView.setOnClickListener {
                val detailIntent = Intent(context, DetailActivity::class.java)
                val clickedItem: Schools = list[adapterPosition]
                //send data to the detailActivity
                detailIntent.putExtra(EXTRA_SCHOOL_NAME, clickedItem.mSchoolName)
                detailIntent.putExtra(EXTRA_SCHOOL_ADDRESS, clickedItem.mPrimaryAddressLine1)
                detailIntent.putExtra(EXTRA_SCHOOL_CITY, clickedItem.mCity)
                detailIntent.putExtra(EXTRA_SCHOOL_EMAIL, clickedItem.mSchoolEmail)
                detailIntent.putExtra(EXTRA_SCHOOL_PHONE, clickedItem.mPhoneNumber)
                detailIntent.putExtra(EXTRA_SCHOOL_OVERVIEW, clickedItem.mOverviewParagraph)
                detailIntent.putExtra(EXTRA_SCHOOL_DBN, clickedItem.dbn)
                detailIntent.putExtra(EXTRA_SCHOOL_WEBSITE, clickedItem.mWebsite)
                context.startActivity(detailIntent)
            }
            //Implicit intent to open the device's phone dialer, particularly will populate with
            // the phone number of the school retrieved from the json
            schoolPhone.setOnClickListener {
                val phoneItem: Schools = list[adapterPosition]
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${phoneItem.mPhoneNumber?.toString()}")
                context.startActivity(intent)

            }
        }
    }

}