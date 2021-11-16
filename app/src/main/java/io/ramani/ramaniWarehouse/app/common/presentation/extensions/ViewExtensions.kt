package io.ramani.ramaniWarehouse.app.common.presentation.extensions

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import io.ramani.ramaniWarehouse.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.backgroundColorResource
import android.os.Handler

/**
 * Created by Amr on 6/27/17.
 */

fun View.visible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.inVisible(inVisible: Boolean) {
    visibility = if (inVisible) View.INVISIBLE else View.VISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.locateOnScreen(): Rect {
    val locArray = IntArray(2)
    getLocationOnScreen(locArray)

    val left = locArray[0]
    val top = locArray[1]
    val right = left + width
    val bottom = top + height

    return Rect(left, top, right, bottom)
}


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(rootView?.windowToken, 0)
}

fun View.showMenuPopup(
    @MenuRes menuRes: Int,
    init: (PopupMenu) -> Unit = {},
    onItemClick: (Int) -> Unit
) {
    val popUpMenu = PopupMenu(context, this)
    popUpMenu.inflate(menuRes)
    init(popUpMenu)
    popUpMenu.setOnMenuItemClickListener {
        onItemClick(it.itemId)
        true
    }
    popUpMenu.show()
}

fun View.showMenuPopup(
    @MenuRes menuRes: Int,
    @StyleRes style: Int,
    init: (PopupMenu) -> Unit = {},
    onItemClick: (Int) -> Unit
) {
    val showContext = ContextThemeWrapper(context, style)
    val popUpMenu = PopupMenu(showContext, this)
    popUpMenu.inflate(menuRes)
    init(popUpMenu)
    popUpMenu.setOnMenuItemClickListener {
        onItemClick(it.itemId)
        true
    }
    popUpMenu.show()
}

fun View.showMenuPopupWithIcon(
    @MenuRes menuRes: Int,
    init: (PopupMenu) -> Unit = {},
    onItemClick: (Int) -> Unit
) {
    val popUpMenu = PopupMenu(context, this)

    try {
        val fields = popUpMenu.javaClass.declaredFields
        for (field in fields) {
            if ("mPopup" == field.name) {
                field.isAccessible = true
                val menuPopupHelper = field.get(popUpMenu)
                val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                val setForceIcons =
                    classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                setForceIcons.invoke(menuPopupHelper, true)
                break
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    popUpMenu.menuInflater.inflate(menuRes, popUpMenu.menu)
    init(popUpMenu)
    popUpMenu.setOnMenuItemClickListener {
        onItemClick(it.itemId)
        true
    }
    popUpMenu.show()
}

fun <T> View.showSelectPopUp(
    items: List<T>,
    initPopUp: (ListPopupWindow) -> Unit = {},
    dismissOnClick: Boolean = true,
    wrapWidth: Boolean = false,
    onItemClick: (ListPopupWindow, T, Int) -> Unit = { _, _, _ -> }
) {

    val stringsList: List<String> = items.map { it.toString() }

    val adapter =
        ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, stringsList)

    val popPup = ListPopupWindow(context)
    popPup.anchorView = this

    popPup.isModal = true

    popPup.height = ViewGroup.LayoutParams.WRAP_CONTENT
    popPup.width = ViewGroup.LayoutParams.WRAP_CONTENT

    popPup.promptPosition = ListPopupWindow.POSITION_PROMPT_BELOW

    if (wrapWidth) {
        popPup.setContentWidth(measureContentWidth(adapter, this, context))
    }

    initPopUp(popPup)

    popPup.setOnItemClickListener { _, _, position, _ ->
        val item = items[position]
        onItemClick(popPup, item, position)
        if (popPup.isShowing && dismissOnClick) {
            popPup.dismiss()
        }
    }
    popPup.setAdapter(adapter)
    popPup.show()
}


private fun measureContentWidth(adapter: ListAdapter, view: View, context: Context): Int {
    var measureparent: ViewGroup? = null
    var maxWidth = 0
    var itemView: View? = null
    var itemType = 0

    val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

    val count = adapter.count
    for (i in 0 until count) {
        val positionType = adapter.getItemViewType(i)
        if (positionType != itemType) {
            itemType = positionType
            itemView = null
        }

        if (measureparent == null) {
            measureparent = FrameLayout(context)
        }

        itemView = adapter.getView(i, itemView, measureparent)
        itemView.measure(widthMeasureSpec, heightMeasureSpec)

        val itemWidth = itemView.measuredWidth

        if (itemWidth > maxWidth) {
            maxWidth = itemWidth
        }
    }

    if (view.width > maxWidth) {
        maxWidth = view.width
    }

    return maxWidth
}

fun Spinner.populateSpinner(
    @ArrayRes items: Int, @LayoutRes itemLayout: Int = android.R.layout.simple_spinner_item,
    @LayoutRes dropDownItemLayout: Int = android.R.layout.simple_spinner_dropdown_item
) {
    populateSpinner(resources.getStringArray(items), itemLayout, dropDownItemLayout)
}

fun Spinner.populateSpinner(
    items: Array<String>, @LayoutRes itemLayout: Int = android.R.layout.simple_spinner_item,
    @LayoutRes dropDownItemLayout: Int = android.R.layout.simple_spinner_dropdown_item
) {
    adapter = ArrayAdapter(context, itemLayout, items).apply {
        setDropDownViewResource(dropDownItemLayout)
    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun TabLayout.onTabSelected(
    onTabSelected: (TabLayout.Tab) -> Unit = {},
    onTabReselected: (TabLayout.Tab) -> Unit = {},
    onTabUnselected: (TabLayout.Tab) -> Unit = {}
) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.apply(onTabSelected)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            tab?.apply(onTabReselected)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            tab?.apply(onTabUnselected)
        }
    })
}

fun Context.color(@ColorRes colorResId: Int) = ContextCompat.getColor(this, colorResId)

fun Context.colorStateList(@ColorRes colorListResId: Int) =
    ContextCompat.getColorStateList(this, colorListResId)

fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableResId)

fun Context.circleDrawable(color: String, @DimenRes size: Int) =
    ShapeDrawable(OvalShape()).apply {
        val shapeSize = resources.getDimensionPixelSize(size)
        intrinsicWidth = shapeSize
        intrinsicHeight = shapeSize
        paint.color = Color.parseColor(color)
        setBounds(0, 0, shapeSize, shapeSize)
    }

fun Context.insetDrawable(
    @DrawableRes drawableResId: Int,
    @DimenRes insetDimenRes: Int
): Drawable? = this.run {
    val inset = resources.getDimensionPixelSize(insetDimenRes)

    val insetDrawable = drawable(drawableResId)
    InsetDrawable(insetDrawable, inset, 0, inset, 0)
}

fun Context.stateListAnimator(@AnimatorRes animatorResId: Int) =
    AnimatorInflater.loadStateListAnimator(this, animatorResId)

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.enableScrolling() {
    this.setOnTouchListener { v, event ->
        if (this.hasFocus()) {
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action) {
                MotionEvent.ACTION_SCROLL -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    true
                }
            }
        }
        false
    }
}

fun String.getColorStateList(): ColorStateList? =
    try {
        ColorStateList.valueOf(
            Color
                .parseColor(this)
        )
    } catch (e: Exception) {
        null
    }

fun String.toColor(): Int? =
    try {
        Color.parseColor(this)
    } catch (ex: Exception) {
        null
    }

fun View.tint(colorResID: Int, context: Context) {
    this.backgroundTintList = ContextCompat.getColorStateList(context, colorResID)
}


fun View.buildSnackbar(message: String, init: Snackbar.() -> Unit = {}): Snackbar =
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply { init() }

fun View.showInfoSnackbar(message: String, init: Snackbar.() -> Unit = {}): Snackbar =
    buildSnackbar(message).apply {
        init()
        show()
    }

fun View.showErrorSnackbar(message: String, init: Snackbar.() -> Unit = {}): Snackbar =
    buildSnackbar(message) {
        this.view.backgroundColorResource = R.color.gloss_red
    }.apply {
        init()
        show()
    }

fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()


fun View.fixedWidth(width: Int) = apply {
    layoutParams.width = width
}

fun View.widthWrapContent() = apply {
    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
}

fun View.setOnSingleClickListener(onClickListener: (view:View) -> Unit) = apply{
    setOnClickListener{
        onClickListener(it)
        isEnabled = false
        Handler().postDelayed({
           isEnabled = true
        },2000)
    }
}

