package jp.cordea.xx

import io.reactivex.*
import java.util.concurrent.TimeUnit

class Engine {

    fun getSingle(): Single<String> =
            getObservable()
                    .firstOrError()

    fun getMaybe(): Maybe<String> =
            getObservable()
                    .firstElement()

    fun getCompletable(): Completable =
            getObservable()
                    .ignoreElements()

    fun getObservable(): Observable<String> =
            Observable
                    .fromIterable(0..20)
                    .map { it.toString() }

    fun getFlowable(strategy: BackpressureStrategy): Flowable<String> =
            Observable
                    .interval(100, TimeUnit.MILLISECONDS)
                    .take(20)
                    .map {
                        it.toString()
                    }
                    .toFlowable(strategy)
}