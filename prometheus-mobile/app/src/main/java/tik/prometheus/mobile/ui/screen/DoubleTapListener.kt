package tik.prometheus.mobile.ui.screen

import android.content.Context
import android.view.View

class DoubleTapListener(val context: Context, callback: (view: View?) -> Unit) : View.OnClickListener {
    private var isRunning = false
    private val resetInTime = 500
    private var counter = 0
    private val listener = callback


    override fun onClick(v: View?) {
        if (isRunning) {
            if (counter == 1) //<-- makes sure that the callback is triggered on double click
                listener.invoke(v)
        }
        counter++
        if (!isRunning) {
            isRunning = true
            Thread {
                try {
                    Thread.sleep(resetInTime.toLong())
                    isRunning = false
                    counter = 0
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }
}
