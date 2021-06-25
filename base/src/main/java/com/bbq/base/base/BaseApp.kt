package com.bbq.base.base

import android.app.Application

open class BaseApp : Application() {

    companion object {
        private lateinit var instance: Application
        fun getInstance(): Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}