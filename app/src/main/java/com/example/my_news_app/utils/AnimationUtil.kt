package com.example.my_news_app.utils

import android.content.Context
import android.view.View

class AnimationUtil {
    companion object {
        fun View.slideInAnimation() {
            val animation = android.view.animation.AnimationUtils.loadAnimation(
                context,
                android.R.anim.slide_in_left
            )
            this.startAnimation(animation)
        }

        fun View.slideOutAnimation() {
            val animation = android.view.animation.AnimationUtils.loadAnimation(
                context,
                android.R.anim.slide_out_right
            )
            this.startAnimation(animation)
        }

        fun View.fadeInAnimation() {
            val animation = android.view.animation.AnimationUtils.loadAnimation(
                context,
                android.R.anim.fade_in
            )
            animation.setInterpolator(context, android.R.anim.linear_interpolator)
            this.startAnimation(animation)
        }
    }
}