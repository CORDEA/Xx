package jp.cordea.xx

import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable

class SourceHandler : HandlerBase() {

    private val handler by lazy {
        BackpressureHandler().also {
            listener?.let(it::setOnOutputLogListener)
        }
    }

    fun handle(source: Source, strategy: BackpressureStrategy) {
        dispose()
        compositeDisposable = CompositeDisposable()
        when (source) {
            Source.OBSERVABLE -> handleObservable()
            Source.SINGLE -> handleSingle()
            Source.MAYBE -> handleMaybe()
            Source.COMPLETABLE -> handleCompletable()
            Source.FLOWABLE -> handleFlowable(strategy)
        }
    }

    override fun dispose() {
        handler.dispose()
        super.dispose()
    }

    private fun handleObservable() {
        engine
                .getObservable()
                .subscribe({
                    listener?.onOutput("onNext %s".format(it))
                }, {
                    listener?.onOutput("onError %s".format(it.localizedMessage))
                }, {
                    listener?.onOutput("onComplete")
                })
                .let(compositeDisposable::add)
    }

    private fun handleSingle() {
        engine
                .getSingle()
                .subscribe({
                    listener?.onOutput("onSuccess %s".format(it))
                }, {
                    listener?.onOutput("onError %s".format(it.localizedMessage))
                })
                .let(compositeDisposable::add)
    }

    private fun handleMaybe() {
        engine
                .getMaybe()
                .subscribe({
                    listener?.onOutput("onSuccess %s".format(it))
                }, {
                    listener?.onOutput("onError %s".format(it.localizedMessage))
                })
                .let(compositeDisposable::add)
    }

    private fun handleCompletable() {
        engine
                .getCompletable()
                .subscribe({
                    listener?.onOutput("onComplete")
                }, {
                    listener?.onOutput("onError %s".format(it.localizedMessage))
                })
                .let(compositeDisposable::add)
    }

    private fun handleFlowable(strategy: BackpressureStrategy) {
        handler.handle(strategy)
    }
}