package com.devgary.contentlinkapi.util

import android.util.Log
import com.devgary.contentcore.util.TAG
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import java.util.concurrent.TimeUnit

object RxAndroidUtils {
    fun <T : Any> setupObservable(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable
                .compose(applyObservableSchedulers())
                .doOnError { throwable: Throwable? -> Log.e(TAG, "", throwable) }
        }
    }

    fun <T : Any> setupMaybe(): MaybeTransformer<T, T> {
        return MaybeTransformer { maybe: Maybe<T> ->
            maybe
                .compose(applyMaybeSchedulers())
                .doOnError { throwable: Throwable? -> Log.e(TAG, "", throwable) }
        }
    }

    fun <T : Any> setupSingle(): SingleTransformer<T, T> {
        return SingleTransformer { single: Single<T> ->
            single
                .compose(applySingleSchedulers())
                .doOnError { throwable: Throwable? -> Log.e(TAG, "", throwable) }
        }
    }

    fun <T> setupCompletable(): CompletableTransformer {
        return CompletableTransformer { completable: Completable ->
            completable
                .compose(applyCompletableSchedulers())
                .doOnError { throwable: Throwable? -> Log.e(TAG, "", throwable) }
        }
    }

    private fun <T : Any> applyObservableSchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun <T : Any> applyMaybeSchedulers(): MaybeTransformer<T, T> {
        return MaybeTransformer { maybe: Maybe<T> ->
            maybe
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun <T : Any> applySingleSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { single: Single<T> ->
            single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun applyCompletableSchedulers(): CompletableTransformer {
        return CompletableTransformer { completable: Completable ->
            completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T : Any> setObservableTimeout(timeoutInMillis: Long): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable
                .timeout(timeoutInMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        }
    }

    fun <T : Any> setSingleTimeout(timeoutInMillis: Long): SingleTransformer<T, T> {
        return SingleTransformer { single: Single<T> ->
            single
                .timeout(timeoutInMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        }
    }
}