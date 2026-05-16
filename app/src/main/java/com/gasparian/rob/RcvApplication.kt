package com.gasparian.rob

import android.app.Application
import com.gasparian.rob.di.rcvAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RcvApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RcvApplication)
            modules(rcvAppModule)
        }
    }
}
