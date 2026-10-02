package com.example.util

import org.json.JSONObject

/** Shared, bounded schema for the editable board. Empty data keeps safe defaults. */
object ScreenCustomization {
    val ids = listOf("logo", "name", "address", "clock", "date", "countdown", "information", "schedule", "ticker")
    data class Component(val visible: Boolean = true, val background: String = "#102B24",
        val color: String = "#F5F7F4", val opacity: Float = 0.75f, val size: Float = 1f, val text: String = "")
    fun component(json: String, id: String): Component {
        val value = runCatching { JSONObject(json).optJSONObject(id) }.getOrNull() ?: JSONObject()
        fun color(key: String, default: String) = value.optString(key).takeIf { it.matches(Regex("#[0-9a-fA-F]{6}")) } ?: default
        return Component(value.optBoolean("visible", true), color("background", "#102B24"), color("color", "#F5F7F4"),
            value.optDouble("opacity", 0.75).takeIf { it.isFinite() }?.coerceIn(0.0, 1.0)?.toFloat() ?: 0.75f,
            value.optDouble("size", 1.0).takeIf { it.isFinite() }?.coerceIn(0.7, 1.4)?.toFloat() ?: 1f,
            value.optString("text", "").take(500))
    }
    fun normalize(json: String): String {
        require(json.length <= 20000) { "Pengaturan layar terlalu besar" }
        require(runCatching { JSONObject(json) }.isSuccess) { "Format pengaturan layar tidak valid" }
        return JSONObject().apply {
            ids.forEach { id -> val c = component(json, id); put(id, JSONObject().apply {
                put("visible", c.visible); put("background", c.background); put("color", c.color)
                put("opacity", c.opacity.toDouble()); put("size", c.size.toDouble()); put("text", c.text)
            }) }
        }.toString()
    }
}
