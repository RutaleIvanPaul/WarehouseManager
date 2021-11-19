package io.ramani.ramaniWarehouse.app.common.presentation.widgets

open class SelfRemovingOnScrollListener : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this)
        }
    }
}