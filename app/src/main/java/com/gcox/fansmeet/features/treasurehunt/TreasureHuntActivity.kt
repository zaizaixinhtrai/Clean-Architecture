package com.gcox.fansmeet.features.treasurehunt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.activity.HandleErrorActivity
import kotlinx.android.synthetic.main.activity_treasure_hunt.*

class TreasureHuntActivity: HandleErrorActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, TreasureHuntActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}