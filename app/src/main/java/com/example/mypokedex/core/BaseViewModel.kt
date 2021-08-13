package com.example.mypokedex.core

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    private val job = SupervisorJob()
    private val bgScope: CoroutineScope = CoroutineScope(Dispatchers.Default + job)

    fun runCommand(completable: suspend () -> Unit) {
        bgScope.launch(SafeCoroutineExceptionHandler { _, throwable ->
            Log.e(
                "coroutine exception",
                "Executing ${this@BaseViewModel::class.simpleName} ${completable::class.java.simpleName} failed",
                throwable
            )
        }) {
            Log.d(
                "coroutine command",
                "Executing command in $ { this@BaseViewModel::class.simpleName }, ${completable::class.simpleName}"
            )
            completable()
        }
    }

    private inline fun SafeCoroutineExceptionHandler(crossinline handler: (CoroutineContext, Throwable) -> Unit): CoroutineExceptionHandler =
        object : AbstractCoroutineContextElement(CoroutineExceptionHandler),
            CoroutineExceptionHandler {
            override fun handleException(context: CoroutineContext, exception: Throwable) {
                if (context.isActive) handler(context, exception)
                else Log.e(
                    "coroutine exception",
                    "Error occurred but the consumer is no longer active",
                    exception
                )
            }

        }

    override fun onCleared() {
        job.cancelChildren()
    }

}
