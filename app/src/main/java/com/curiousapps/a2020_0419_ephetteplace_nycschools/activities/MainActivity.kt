package com.curiousapps.a2020_0419_ephetteplace_nycschools.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.curiousapps.a2020_0419_ephetteplace_nycschools.R
import com.curiousapps.a2020_0419_ephetteplace_nycschools.data.*
import com.curiousapps.a2020_0419_ephetteplace_nycschools.model.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException

class MainActivity : BaseActivity() {

    private lateinit var schoolUrl: String
    private var volleyRequest: RequestQueue? = null
    private var schoolsArray: ArrayList<Schools>? = null
    private var mSchoolListAdapter: SchoolListAdapter? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mProgressStatus: Int = 0

    private lateinit var schoolEmail: String
    private lateinit var schoolWebLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSchoolUrl()
        getSchools(schoolUrl)
    }

    private fun getSchoolUrl(){
        //build a usable url for volley
        val tempUrl = SCHOOL_BASE_URL + SCHOOL_EXTENSION_URL + TOKEN_PREFIX + APP_TOKEN
        schoolUrl = tempUrl
    }

    private fun getSchools(Url: String){
        volleyRequest = Volley.newRequestQueue(this)
        showProgressBar(true)//progress bar to let the user know the app is working on the request

        schoolsArray = ArrayList<Schools>()
        val schoolRequest = JsonArrayRequest(Request.Method.GET, Url, null,
                Response.Listener { response: JSONArray ->
                    try {
                        Log.d("<_-_-_-Response-_-_-_>", response.length().toString() + ": " + schoolUrl)
                        for (i in 0 until response.length()) {
                            val schoolObj = response.getJSONObject(i)
                                // bind data to a string reference
                            val schoolDbn = schoolObj.getString("dbn")
                            val schoolName = schoolObj.getString("school_name")
                            val schoolAddress = schoolObj.getString("primary_address_line_1")
                            val schoolCity = schoolObj.getString("city")
                            val schoolState = schoolObj.getString("state_code")
                            val schoolZip = schoolObj.getString("zip")
                            val schoolPhone = schoolObj.getString("phone_number")
                            val schoolOverview = schoolObj.getString("overview_paragraph")
                            // Conditional checks because not all schools have website, and email json fields
                            if (schoolObj.has("website")){
                                schoolWebLink = schoolObj.getString("website")
                            }
                            if (schoolObj.has("school_email")){
                                schoolEmail = schoolObj.getString("school_email")
                            }

                            Log.d("###RESULT###", schoolName)
                            Log.d(schoolName, schoolDbn)
                            Log.d(schoolName, schoolWebLink)
                            Log.d(schoolName, schoolEmail)
                            //values populate an array
                            val school = Schools()
                            school.dbn = schoolDbn
                            school.mSchoolName = schoolName
                            school.mPrimaryAddressLine1 = schoolAddress
                            school.mCity = schoolCity
                            school.mStateCode = schoolState
                            school.mZip = schoolZip
                            school.mPhoneNumber = schoolPhone
                            school.mSchoolEmail = schoolEmail
                            school.mWebsite = schoolWebLink
                            school.mOverviewParagraph = schoolOverview

                            schoolsArray?.add(school)
                            mSchoolListAdapter = SchoolListAdapter(schoolsArray!!, this)
                            mLayoutManager = LinearLayoutManager(this)

                            setupRecyclerView()
                        }
                        mSchoolListAdapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    showProgressBar(false)
                }, Response.ErrorListener { error -> error.printStackTrace() })
        volleyRequest?.add(schoolRequest)
    }

    private fun setupRecyclerView(){
        school_list_recycler_view.layoutManager = mLayoutManager
        school_list_recycler_view.adapter = mSchoolListAdapter
    }
}
