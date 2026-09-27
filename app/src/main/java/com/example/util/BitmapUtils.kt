package com.example.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

object BitmapUtils {

    /**
     * Decodes a sampled bitmap from a local file to avoid OutOfMemoryError on Android TV / STB.
     * Automatically downsamples large images and uses RGB_565 for reduced memory footprint.
     */
    fun decodeSampledBitmapFromFile(
        filePath: String,
        reqWidth: Int = 1920,
        reqHeight: Int = 1080
    ): ImageBitmap? {
        if (filePath.isBlank()) return null
        val file = File(filePath)
        if (!file.exists() || !file.isFile) return null

        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.absolutePath, options)

            if (options.outWidth <= 0 || options.outHeight <= 0) return null

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565

            val decodedBitmap = BitmapFactory.decodeFile(file.absolutePath, options)
            decodedBitmap?.asImageBitmap()
        } catch (_: Throwable) {
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize.coerceAtLeast(1)
    }
}
