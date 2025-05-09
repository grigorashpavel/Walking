package ru.pasha.walking

import android.app.Application
import ru.pasha.common.initImageLoader

class WalkingApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initImageLoader(applicationContext)
    }
}
