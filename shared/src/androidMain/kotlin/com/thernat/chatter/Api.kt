package com.thernat.chatter

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.websocket.*
import kotlinx.coroutines.*

internal actual val ApplicationDispatcher: CoroutineDispatcher = Dispatchers.IO

actual var client: HttpClient? = HttpClient(OkHttp) {
    install(WebSockets)
}