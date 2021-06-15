package com.alvaro.finalApp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.alvaro.finalApp.R
import com.alvaro.finalApp.adapters.PagerAdapter
import com.alvaro.finalApp.fragments.ChatFragment
import com.alvaro.finalApp.fragments.InfoFragment
import com.alvaro.finalApp.fragments.RatesFragment
import com.alvaro.finalApp.goToActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    private var prevBottomSelected : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarToLoad(toolbarView as Toolbar)
        setUpPagerAdapter(getPagerAdapter())
        setUpBottonNavBar()
    }

    private fun getPagerAdapter():PagerAdapter{
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())
        return adapter
    }

    private fun setUpPagerAdapter(adapter:PagerAdapter){

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = adapter.count
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float,positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null){
                    bottomNav.menu.getItem(0).isChecked = false
                }else{
                    prevBottomSelected!!.isChecked = false
                }
                bottomNav.menu.getItem(position).isChecked = true
                prevBottomSelected = bottomNav.menu.getItem(position)
            }
        })
    }

    private fun setUpBottonNavBar(){
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_nav_info ->  {viewPager.currentItem = 0; true}
                R.id.bottom_nav_rates ->  {viewPager.currentItem = 1; true}
                R.id.bottom_nav_chat -> {viewPager.currentItem = 2; true}
            else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.general_option_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_log_out -> {
                FirebaseAuth.getInstance().signOut()
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

}