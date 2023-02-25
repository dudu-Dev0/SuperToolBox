package com.dudu.supertoolbox.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Parcel
import android.os.Parcelable

class ToolBoxApplication() :Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}