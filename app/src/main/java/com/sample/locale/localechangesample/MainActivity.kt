package com.sample.locale.localechangesample

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val DEFAULT_SUPPORTED_LANGUAGE: String = "en"
    private val OTHER_LANGUAGE: String = "pt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            updateResources()
            refreshText()
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    @SuppressLint("WrongViewCast")
    private fun refreshText() {
        findViewById<TextView>(R.id.superText).text = getStringResourceFromResources(R.string.text_to_change)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleContextAware(newBase).wrap())
    }

    private fun updateResources() {
        //DEFAULT LANGUAGE SUPPORTED IS EN for us
        var currentLocale: Locale = Locale(DEFAULT_SUPPORTED_LANGUAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = getLocaleToUpdate()
        }

        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(currentLocale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales = LocaleList(currentLocale)
            LocaleList.setDefault(LocaleList(currentLocale))
            createConfigurationContext(configuration)
        } else {
            Locale.setDefault(Locale(currentLocale.language, currentLocale.country.toUpperCase()))
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun  getLocaleToUpdate(): Locale {
        val locale = resources.configuration.locales.get(0)
        if (locale.language == Locale(DEFAULT_SUPPORTED_LANGUAGE).language){
            return Locale(OTHER_LANGUAGE)
        }else{
            return Locale(DEFAULT_SUPPORTED_LANGUAGE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getStringResourceFromResources(key: Int): String? {
        try {
            //mContext is the context of the activity
            return createConfigurationContext(resources.configuration).getString(key)
        } catch (e: Resources.NotFoundException) {
            return null
        }

    }
}
