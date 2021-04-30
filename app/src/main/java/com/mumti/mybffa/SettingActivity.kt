package com.mumti.mybffa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.title = "Setting"

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
        }
    }
}