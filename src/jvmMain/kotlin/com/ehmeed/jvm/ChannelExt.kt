package com.ehmeed.jvm

import kotlinx.coroutines.channels.Channel


fun <T : Any> Channel<T>.pollAll(): Sequence<T> {
    return generateSequence { this.poll() }
        .takeWhile { it != null }
}
