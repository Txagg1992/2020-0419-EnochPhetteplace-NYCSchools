package com.curiousapps.a2020_0419_ephetteplace_nycschools.activities

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.curiousapps.a2020_0419_ephetteplace_nycschools.R

// Set up a base activity for all activities in the application
abstract class BaseActivity : AppCompatActivity() {
    var mProgressBar //available for any activity that needs it
            : ProgressBar? = null

    override fun setContentView(layoutResID: Int) {
        //Constraint Layout with a frame layout for a container to place activities inside
        val constraintLayout =
            layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout
        val frameLayout =
            constraintLayout.findViewById<FrameLayout>(R.id.activity_content)
        mProgressBar = constraintLayout.findViewById(R.id.progress_bar)
        layoutInflater.inflate(layoutResID, frameLayout, true)
        super.setContentView(constraintLayout)
    }

    //Condition for showing progress bar
    fun showProgressBar(visibility: Boolean) {
        mProgressBar!!.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }
}