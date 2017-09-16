package jp.cordea.xx

import io.reactivex.BackpressureStrategy
import io.reactivex.FlowableSubscriber
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription

class BackpressureHandler : HandlerBase() {

    fun handle(strategy: BackpressureStrategy) {
        dispose()
        compositeDisposable = CompositeDisposable()
        engine
                .getFlowable(strategy)
                .doOnRequest { listener?.onOutput("onRequest") }
                .onBackpressureDrop { listener?.onOutput("onBackpressureDrop") }
                .subscribeOn(Schedulers.computation())
                .subscribe(object : FlowableSubscriber<String> {

                    private lateinit var subscription: Subscription

                    override fun onError(t: Throwable) {
                        listener?.onOutput("onError %s".format(t.localizedMessage))
                    }

                    override fun onComplete() {
                        listener?.onOutput("onComplete")
                    }

                    override fun onNext(t: String) {
                        listener?.onOutput("onNext %s".format(t))
                        Thread.sleep(500)
                        subscription.request(1)
                    }

                    override fun onSubscribe(s: Subscription) {
                        subscription = s
                        subscription.request(1)
                    }
                })
    }
}