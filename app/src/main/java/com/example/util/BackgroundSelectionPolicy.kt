package com.example.util

/** A full settings form may resend the preset while an image/video/CCTV is active. */
object BackgroundSelectionPolicy {
    fun resolveType(currentType: String, currentPreset: String, params: Map<String, String>): String {
        val explicit = params["backgroundType"]
        if (explicit in setOf("PRESET", "IMAGE", "VIDEO", "CCTV")) return explicit!!
        val preset = params["mainScreenBgPreset"]
        if (preset != null && preset != currentPreset) {
            return if (preset == "CUSTOM_UPLOAD") "IMAGE" else "PRESET"
        }
        return currentType
    }
}
