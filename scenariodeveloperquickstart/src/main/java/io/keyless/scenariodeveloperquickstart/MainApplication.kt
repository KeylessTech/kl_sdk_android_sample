package io.keyless.scenariodeveloperquickstart

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // using single instance of Keyless SDK, you can use a DI framework instead
        ContentViewModel.keylessWrapper.initialize(application = this)
    }
}