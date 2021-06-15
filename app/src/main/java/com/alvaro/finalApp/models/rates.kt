package com.alvaro.finalApp.models

import java.util.*

data  class rates( val userId:String = "",
                   val text:String = "",
                  val rate:Float = 0f,
                  val createdAt:Date = Date(),
                  val profileImageUrl: String = "",

                  )