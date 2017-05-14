package com.nao20010128nao.GroovyRoom.settings

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.ActionBar
import android.view.MenuItem
import com.nao20010128nao.GroovyRoom.Constants
import com.nao20010128nao.GroovyRoom.R
import groovy.transform.CompileStatic

/**
 * An activity representing a single Setting detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SettingListActivity}.
 */
@CompileStatic
class SettingDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_detail)
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar)
        setSupportActionBar(toolbar)

        if (supportActionBar) {
            supportActionBar.displayHomeAsUpEnabled=true
        }


        def clazz=(Class)intent.getSerializableExtra(Constants.EXTRA_FRAGMENT_CLASS)
        def fragment=(Fragment)clazz.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.setting_detail_container, fragment)
                .commit()
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        int id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}