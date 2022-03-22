package com.thernat.chatter

import io.ktor.client.*
import kotlin.coroutines.*
import kotlinx.coroutines.*
import platform.darwin.*

internal actual val ApplicationDispatcher: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())

internal actual var client: HttpClient? = null

internal class NsQueueDispatcher(
    private val dispatchQueue: dispatch_queue_t
) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatchQueue) {
            block.run()
        }
    }
}