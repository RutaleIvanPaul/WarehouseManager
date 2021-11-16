package io.ramani.ramaniWarehouse.app.common.presentation.interfaces

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * an interface implemented by base classes that holds multiple disposables
 *
 * Created by Ahmed Adel Ismail on 2/24/2018.
 */
interface DisposablesHolder {
    val compositeDisposable: CompositeDisposable
}


fun DisposablesHolder.addDisposable(disposable: Disposable) {
    compositeDisposable.add(disposable)
}

fun DisposablesHolder.removeDisposable(disposable: Disposable) {
    compositeDisposable.remove(disposable)
}

fun DisposablesHolder.clearDisposables() {
    compositeDisposable.clear()
}

fun DisposablesHolder.disposeAll() {
    compositeDisposable.dispose()
}



