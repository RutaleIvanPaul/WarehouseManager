package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ViewPager(mContext: Context, attributes: AttributeSet) : ViewPager(mContext, attributes) {


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean =
            try {
                super.onInterceptTouchEvent(ev)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                false
            }

}