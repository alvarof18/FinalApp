package com.alvaro.finalApp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.alvaro.finalApp.interfaces.Itoolbar

open class ToolbarActivity : AppCompatActivity(), Itoolbar {
    protected var _toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun toolbarToLoad(toolbar: Toolbar) {
        _toolbar = toolbar
        _toolbar.let {
            setSupportActionBar(_toolbar)
        }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }
}