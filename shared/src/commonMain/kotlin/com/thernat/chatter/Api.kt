package com.thernat.chatter

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal expect val ApplicationDispatcher: CoroutineDispatcher

internal expect var client: HttpClient?

class AppApi {

     private val msgFlow = MutableStateFlow("")
     private var myNickname: String? = null


     private val initializedClient = client ?: HttpClient {
          install(WebSockets)
          install(JsonFeature)
     }

     fun chat(callback: (String, String, Boolean) -> Unit) {
          GlobalScope.launch(ApplicationDispatcher) {
               msgFlow.collect {
                    when {
                         it.isEmpty() -> {}
                         it.startsWith(SERVER_WELCOME) -> {
                              myNickname = it.substringAfter("know as ")
                         }
                         else -> {
                              Json.decodeFromString<MessageModel>(it).run {
                                   callback(from, content, from == myNickname)
                              }
                         }
                    }
               }
          }
          GlobalScope.launch(ApplicationDispatcher) {
               initializedClient.ws(
                    method = HttpMethod.Get,
                    host = "192.168.1.29",
                    port = 8080,
                    path = "/companion"
               ) {
                    outputMessages()
               }
          }
     }


     private suspend fun DefaultClientWebSocketSession.outputMessages() {
          try {
               for (msg in incoming) {
                    val newMsg = msg as? Frame.Text ?: continue
                    msgFlow.value = newMsg.readText()
               }
          } catch (e: Exception) {
               println("Exception on output message " + e.message)
          }
     }

     suspend fun DefaultClientWebSocketSession.inputMessages() {
          while (true) {
               ("Hello").let { msg ->
                    if (msg.toLowerCase() == "quit") return
                    try {
                         send(msg)
                    } catch (e: Exception) {
                         println("Exception during input message: " + e.message)
                         return
                    }
               }
          }
     }

     companion object {
          const val SERVER_WELCOME = "You are connected!"
     }
}
@Serializable
data class MessageModel(val from : String ,val content: String, val timeStamp: String)

