package com.example.trainingtracker

object EventManager {
    private val subscribers = mutableMapOf<String, MutableList<(Event) -> Unit>>()

    fun subscribe(eventType: String, subscriber: (Event) -> Unit) {
        if (!subscribers.containsKey(eventType)) {
            subscribers[eventType] = mutableListOf()
        }
        subscribers[eventType]?.add(subscriber)
    }

    fun unsubscribe(eventType: String, subscriber: (Event) -> Unit) {
        subscribers[eventType]?.remove(subscriber)
    }

    fun publish(event: Event) {
        subscribers[event.type]?.forEach { it.invoke(event) }
    }
}
