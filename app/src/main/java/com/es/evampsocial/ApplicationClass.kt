package com.es.evampsocial

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex


class ApplicationClass : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    override fun onCreate() {
        super.onCreate()


    }
}