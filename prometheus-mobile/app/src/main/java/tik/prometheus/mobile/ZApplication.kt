package tik.prometheus.mobile

import android.app.Application

class ZApplication : Application() {
    lateinit var container: ZContainer

    fun initContainer() {
        container = ZContainer()
    }
}