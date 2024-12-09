package com.exal.grocerease.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun File.compressFile(context: Context): File? {
    try {
        val bitmap = BitmapFactory.decodeFile(this.path)

        if (bitmap == null) {
            Log.e("compressFile", "Failed to decode file into Bitmap.")
            return null
        }

        val compressedFile = reduceFileImage(bitmap, context)

        val outputStream = FileOutputStream(compressedFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // 80% quality
        outputStream.flush()
        outputStream.close()

        return compressedFile
    } catch (e: Exception) {
        Log.e("compressFile", "Error compressing file: ${e.message}")
        return null
    }
}

fun reduceFileImage(bitmap: Bitmap, context: Context): File {
    val MAXIMAL_SIZE = 2 * 1024 * 1024 // 2MB
    var compressQuality = 100
    var streamLength: Int
    val bmpStream = ByteArrayOutputStream()

    do {
        bmpStream.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)

    val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) // You can use INTERNAL_STORAGE if needed
    val compressedFile = File(outputDir, "compressed_image_${System.currentTimeMillis()}.jpg")

    val outputStream = FileOutputStream(compressedFile)
    outputStream.write(bmpStream.toByteArray())
    outputStream.flush()
    outputStream.close()

    return compressedFile
}
