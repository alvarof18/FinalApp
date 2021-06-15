package com.alvaro.finalApp.models

import java.util.*

data class Message(val authorId: String = " ",
                   val message: String = "",
                   val profileImageUrl:String="",
                   val SendAt:Date = Date()) {
}