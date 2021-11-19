package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.viewpager.widget.ViewPager


/**
 * Created by Raed Ezzat on 20/12/2017.
 */
class TouchGreedyViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private var xDistance: Float = 0.toFloat()
    private var yDistance: Float = 0.toFloat()
    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                yDistance = 0f
                xDistance = yDistance
                lastX = ev.x
                lastY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val curX = ev.x
                val curY = ev.y
                xDistance += Math.abs(curX - lastX)
                yDistance += Math.abs(curY - lastY) / 3 // favor X events
                lastX = curX
                lastY = curY
                if (xDistance > yDistance) {
                    return true
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }
}

class TouchHumbleScrollView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {
    private var xDistance: Float = 0.toFloat()
    private var yDistance: Float = 0.toFloat()
    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                yDistance = 0f
                xDistance = yDistance
                lastX = ev.x
                lastY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val curX = ev.x
                val curY = ev.y
                xDistance += Math.abs(curX - lastX)
                yDistance += Math.abs(curY - lastY) / 3 // favor X events
                lastX = curX
                lastY = curY
                if (xDistance > yDistance) {
                    return false
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }
}