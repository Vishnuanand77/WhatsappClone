package com.vishnu.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
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
        supportActionBar!!.elevation = 0F

        //Tab View and ViewPager configurations
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        dashboardViewPager.adapter = sectionPagerAdapter
        mainTabs.setupWithViewPager(dashboardViewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        //Inflating the menu.xml file
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.menu_settings) {
            //Open Settings page
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        if (item.itemId == R.id.menu_logout) {
            //Logout user
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return true
    }


}