package com.ehmeed.js

import org.w3c.dom.events.EventTarget

fun EventTarget.value(): String? = this.asDynamic()?.value?.unsafeCast<String?>()
