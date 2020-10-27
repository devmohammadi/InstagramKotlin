package com.fmohammadi.instagram.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fmohammadi.instagram.R
import com.fmohammadi.instagram.fragment.HomeFragment
import com.fmohammadi.instagram.fragment.NotificlationFragment
import com.fmohammadi.instagram.fragment.ProfileFragment
import com.fmohammadi.instagram.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectorFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> selectorFragment = HomeFragment()
                R.id.nav_search -> selectorFragment = SearchFragment()
                R.id.nav_like -> selectorFragment = NotificlationFragment()
                R.id.nav_profile -> selectorFragment = ProfileFragment()
                R.id.nav_add -> {
                    selectorFragment = null
                    startActivity(Intent(this@MainActivity, PostActivity::class.java))
                }
            }

            if (selectorFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectorFragment!!).commit()
            }
            return@setOnNavigationItemSelectedListener true
        }
        //default fragment when activity start
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()
    }
}