package jp.cordea.xx

import io.reactivex.disposables.CompositeDisposable

abstract class HandlerBase {

    interface OnOutputLogListener {
        fun onOutput(log: String)
    }

    protected val engine = Engine()

    protected var compositeDisposable = CompositeDisposable()

    protected var listener: OnOutputLogListener? = null

    open fun dispose() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    fun setOnOutputLogListener(listener: OnOutputLogListener) {
        this.listener = listener
    }
}