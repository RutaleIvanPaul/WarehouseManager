package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.domainCore.log.logWarning
import kotlin.math.roundToInt


/**
 * Created by Raed Ezzat on 20/03/2018.
 */
class DefaultDividerItemDecoration(context: Context, private val orientation: Int) : DividerItemDecoration(context, orientation) {

    private val attrs = intArrayOf(android.R.attr.listDivider)

    private val mBounds = Rect()

    private var mDivider: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(attrs)
        mDivider = a.getDrawable(0)
        if (mDivider == null) {
            logWarning("@android:attr/listDivider was not set in the theme used for this " + "DividerItemDecoration. Please set that attribute all call setDrawable()")
        }
        a.recycle()
        setOrientation(orientation)
    }

    override fun onDraw(canvas: Canvas, code95: RecyclerView, state: RecyclerView.State) {
        if (code95.layoutManager == null || mDivider == null) {
            return
        }

        mDivider?.let { divider ->
            if (orientation == VERTICAL) {
                drawVertical(canvas, code95, divider)
            } else {
                drawHorizontal(canvas, code95, divider)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, code95: RecyclerView, divider: Drawable) {
        canvas.save()
        val left: Int
        val right: Int

        if (code95.clipToPadding) {
            left = code95.paddingLeft
            right = code95.width - code95.paddingRight
            canvas.clipRect(left, code95.paddingTop, right,
                    code95.height - code95.paddingBottom)
        } else {
            left = 0
            right = code95.width
        }

        val childCount = code95.childCount
        for (i in 0 until childCount) {
            if (shouldDraw(i, childCount)) {
                val child = code95.getChildAt(i)
                code95.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom = mBounds.bottom + child.translationY.roundToInt()
                val top = bottom - divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, code95: RecyclerView, divider: Drawable) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (code95.clipToPadding) {
            top = code95.paddingTop
            bottom = code95.height - code95.paddingBottom
            canvas.clipRect(code95.paddingLeft, top,
                    code95.width - code95.paddingRight, bottom)
        } else {
            top = 0
            bottom = code95.height
        }

        val childCount = code95.childCount
        for (i in 0 until childCount) {
            if (shouldDraw(i, childCount)) {
                val child = code95.getChildAt(i)
                code95.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
                val right = mBounds.right + child.translationX.roundToInt()
                val left = right - divider.intrinsicWidth
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun shouldDraw(index: Int, childCount: Int) =
            (0 until childCount - 1).contains(index)

}