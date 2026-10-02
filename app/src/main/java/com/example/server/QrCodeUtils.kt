package com.example.server

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QrCodeUtils {

    /**
     * Generates a standard ISO/IEC 18004 compliant QR Code bitmap for the given URL/content.
     * Easily readable by any smartphone camera.
     */
    fun generateQrBitmap(
        content: String,
        size: Int = 200,
        darkColor: Int = Color.parseColor("#06281D"), // Deep Islamic Emerald
        lightColor: Int = Color.WHITE
    ): ImageBitmap {
        if (content.isBlank()) {
            val emptyBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            emptyBitmap.eraseColor(lightColor)
            return emptyBitmap.asImageBitmap()
        }

        return try {
            val hints = mapOf(
                EncodeHintType.CHARACTER_SET to "UTF-8",
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
                EncodeHintType.MARGIN to 1 // 1-module quiet zone
            )

            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) darkColor else lightColor
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            val fallback = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            fallback.eraseColor(lightColor)
            fallback.asImageBitmap()
        }
    }
}
