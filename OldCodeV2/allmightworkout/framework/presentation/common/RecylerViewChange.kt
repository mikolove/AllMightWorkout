package com.mikolove.allmightworkout.framework.presentation.common

/*
    Credit to : https://blog.undabot.com/recyclerview-time-to-animate-with-payloads-and-diffutil-4278beb8d4dd
 */
data class Change<out T>(
    val oldData: T,
    val newData: T
)

fun <T> createCombinedPayload(payloads: List<Change<T>>): Change<T> {
    assert(payloads.isNotEmpty())
    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Change(firstChange.oldData, lastChange.newData)
}
