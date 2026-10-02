package com.example.util

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/** Local sessions expire and are revoked when the administrator changes the PIN. */
class LocalAdminSessions(private val now: () -> Long = System::currentTimeMillis) {
    private val sessions = ConcurrentHashMap<String, Long>()
    fun issue(): String {
        sessions.entries.removeIf { it.value <= now() }
        val token = UUID.randomUUID().toString() + UUID.randomUUID().toString()
        sessions[token] = now() + 8 * 60 * 60 * 1000L
        return token
    }
    fun contains(token: String): Boolean {
        val expires = sessions[token] ?: return false
        if (expires <= now()) { sessions.remove(token); return false }
        return true
    }
    fun remove(token: String) { sessions.remove(token) }
    fun clear() { sessions.clear() }
}
