package com.sample.locale.localechangesample

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * @author lpereira on 04/08/2017.
 */

class LocaleContextAware(val context: Context) : ContextWrapper(context) {
    /**
     * Transforms the given context with a context with locale
     * @param context activity context
     */
    fun wrap(): ContextWrapper {
        val res = context.resources
        val configuration = res.configuration
        val newLocale = systemLocale(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale)

            val localeList = LocaleList(newLocale)
            LocaleList.setDefault(localeList)
            configuration.locales = localeList

            return ContextWrapper(context.createConfigurationContext(configuration))

        } else {
            configuration.setLocale(newLocale)
            return ContextWrapper(context.createConfigurationContext(configuration))

        }
    }

    /**
     * Get the current system locale
     */
    fun systemLocale(context: Context): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.resources.configuration.locales.get(0)
        } else {
            return Locale.getDefault()
        }
    }
}
