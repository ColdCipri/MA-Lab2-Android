package com.example.ma_lab2_android

import android.app.Application
import io.realm.Realm

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}