package io.ramani.ramaniWarehouse.core.domain.presentation.drawables

interface IDrawableProvider {

    fun getLeavesGraphStatesDrawable(): Int

    fun getNurseryInfoStatesDrawable(): Int

    fun getPlansStatesDrawable(): Int

    fun getInvoicesStatesDrawable(): Int

}