package io.ramani.ramaniWarehouse.app.common.livedata

import androidx.lifecycle.MutableLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object LiveDataDelegates {
    public fun <T : Any?> liveData(initValue: T?, liveData: MutableLiveData<T>): ReadWriteProperty<Any?, T?> =
            LiveDataVar(initValue, liveData)
}

private class LiveDataVar<T : Any?>(initValue: T?, private val liveData: MutableLiveData<T>) : ReadWriteProperty<Any?, T?> {
    private var value = initValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? =
            value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
        value?.apply {
            liveData.postValue(this)
        }
    }

}