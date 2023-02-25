package com.example.my_news_app.presentation.custom

import android.os.Handler
import android.os.Looper
import com.example.my_news_app.constants.Constants
import java.util.*

class ViewPagerAutoSlider {
    private var handler: Handler? = null
    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null

    fun onAttach() {
        handler = Handler(Looper.myLooper()!!)
    }

    fun onDetach() {
        mTimer?.cancel()
        mTimer = null
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    fun onViewPagerVisible(
        getViewPagerCurrentItem: () -> Int,
        setViewPagerCurrentItem: (Int) -> Unit
    ) {
        mTimer = Timer()
        mTimerTask?.cancel()
        mTimerTask = MyTimerTask(getViewPagerCurrentItem, setViewPagerCurrentItem)
        mTimer?.schedule(
            mTimerTask,
            Constants.VIEWPAGER_TIME_DELAY,
            Constants.VIEWPAGER_TIME_PERIOD
        )
    }

    fun onViewPagerHidden() {
        mTimerTask?.cancel()
        mTimer?.cancel()
        mTimerTask = null
        handler?.removeCallbacksAndMessages(null)
    }

    inner class MyTimerTask(
        private val getCurrentItemPosition: () -> Int,
        private val setCurrentItemPosition: (Int) -> Unit
    ) : TimerTask() {

        override fun run() {
            handler?.post { setCurrentItemPosition((getCurrentItemPosition() + 1) % 3) }
        }
    }
}