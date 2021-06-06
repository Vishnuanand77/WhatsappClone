package com.vishnu.whatsappclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vishnu.whatsappclone.R
import com.vishnu.whatsappclone.adapters.SectionPagerAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private var sectionPagerAdapter: SectionPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //Setting the app bar title
        supportActionBar!!.title = "Dashboard"

        //Tab View and ViewPager configurations
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        dashboardViewPager.adapter = sectionPagerAdapter
        mainTabs.setupWithViewPager(dashboardViewPager)

    }
}