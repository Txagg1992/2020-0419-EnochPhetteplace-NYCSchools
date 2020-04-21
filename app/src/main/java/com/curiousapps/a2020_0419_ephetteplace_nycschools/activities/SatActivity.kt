package com.curiousapps.a2020_0419_ephetteplace_nycschools.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.curiousapps.a2020_0419_ephetteplace_nycschools.R
import com.curiousapps.a2020_0419_ephetteplace_nycschools.data.SAT_EXTENSION_URL
import com.curiousapps.a2020_0419_ephetteplace_nycschools.data.SCHOOL_BASE_URL
import com.curiousapps.a2020_0419_ephetteplace_nycschools.model.SatScores
import kotlinx.android.synthetic.main.activity_sat.*
import org.json.JSONArray
import org.json.JSONException
import kotlin.collections.ArrayList

class SatActivity : BaseActivity() {

    private var satExtra: Bundle? = null
    private lateinit var satScoreUrl: String
    private var satScoreArray: ArrayList<SatScores>? = null
    private var volleyRequest: RequestQueue? = null
    private lateinit var schoolSatSchoolName: String
    private lateinit var schoolSatNumberOfTakers: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sat)

        getSatExtras()
        getSatScores(satScoreUrl)
    }

    private fun getSatScores(Url: String) {
        volleyRequest = Volley.newRequestQueue(this)
        showProgressBar(true)//progress bar to show the user the app is working on the request

        satScoreArray = ArrayList<SatScores>()
        val satRequest = JsonArrayRequest(Request.Method.GET, Url, null,
            Response.Listener { response: JSONArray ->
                try {
                    Log.d("***Sat-Response***", response.toString())
                    if (response.isNull(0 )){
                        Toast.makeText(this,
                            getString(R.string.no_sat_reported),
                            Toast.LENGTH_LONG).show()
                    }
                    for (i in 0 until response.length()) {//Loop through the entire json to build an array
                        val satObject = response.getJSONObject(i)
                            //bind data to a string reference
                        val schoolSatDbn = satObject.getString("dbn")
                        schoolSatSchoolName = satObject.getString("school_name")
                        schoolSatNumberOfTakers = satObject.getString("num_of_sat_test_takers")
                        val schoolSatReadScore =
                            satObject.getString("sat_critical_reading_avg_score")
                        val schoolSatMathScore = satObject.getString("sat_math_avg_score")
                        val schoolSatWritingScore = satObject.getString("sat_writing_avg_score")

                        Log.d("Result", "$schoolSatSchoolName: $schoolSatDbn")
                            //set up values for array
                        val saTs = SatScores()
                        saTs.dbn = schoolSatDbn
                        saTs.sSchoolName = schoolSatSchoolName
                        saTs.sNumberOfTestTakers = schoolSatNumberOfTakers
                        saTs.sReadingAverage = schoolSatReadScore
                        saTs.sMathAverage = schoolSatMathScore
                        saTs.sWritingAverage = schoolSatWritingScore
                            //populate array
                        satScoreArray?.add(saTs)
                            //populate views. slight unconventional concatenation to format views for UX
                        sat_school_name.text = saTs.sSchoolName
                        sat_number_of_tests.text =
                            getString(R.string.sat_tests_taken) + " " + saTs.sNumberOfTestTakers
                        sat_reading_average.text =
                            getString(R.string.reading_average) + " " + saTs.sReadingAverage
                        sat_writing_average.text =
                            getString(R.string.writing_average) + " " + saTs.sWritingAverage
                        sat_math_average.text =
                            getString(R.string.math_average) + " " + saTs.sMathAverage
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                showProgressBar(false)
            }, Response.ErrorListener { error -> error.printStackTrace() })
        volleyRequest?.add(satRequest)
    }

    private fun getSatExtras() {

        /*
        This method retrieves the school's dbn, which is the only constant between the Json urls.
        Then builds a usable Url that will dynamically change for each school based in its dbn.
        The conditional statement retrieves the dbn if there is one. If not, the else statement
        sends the user a toast message.

        Originally I thought to use a Room Database with the dbn as a key for both tables, however
        since data caching or persistence did not seem necessary, I decided to use the dbn as a
        common key from the retrieval of the schools json to populate the url of the SAT json.
         */
        satExtra = intent.extras
        if (satExtra != null) {
            val schoolDbn = satExtra!!.getString("dbn")
            val tempUrl = SCHOOL_BASE_URL + SAT_EXTENSION_URL + schoolDbn
            satScoreUrl = tempUrl
            Log.d("SATURL", tempUrl)
        }else{
            Toast.makeText(this,
                "This school has not reported any SAT scores for the given year",
            Toast.LENGTH_LONG).show()
        }
    }
}
