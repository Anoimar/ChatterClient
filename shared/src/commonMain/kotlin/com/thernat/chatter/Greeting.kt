package com.thernat.chatter

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform.reversed()}!"
    }
}