package com.example.my_news_app.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class UiHelper {
    companion object {
        fun Activity.getInputMethodManager(): InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        fun View.showKeyBoard(activity: Activity) =
            activity.getInputMethodManager().showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

        fun View.hideKeyBoard(activity: Activity) =
            activity.getInputMethodManager().hideSoftInputFromWindow(this.windowToken, 0)

        fun Fragment.launchFragment(
            view: View,
            fragmentManager: FragmentManager,
            addToBackStack: Boolean = false
        ) {
            val transaction = fragmentManager.beginTransaction()
            if (addToBackStack) transaction.addToBackStack("${this.javaClass.simpleName}")
            transaction.add(view.id, this@launchFragment)
            transaction.commit()
        }

        fun FragmentManager.getTopFragment() =
            this.getBackStackEntryAt(this.backStackEntryCount - 1).name

    }
}