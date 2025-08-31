package com.agcoding.oral.utils

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class MediaStoreStorage(private val context: Context) {
    private val appName = "Oral"

    private val sessionsDir = "${Environment.DIRECTORY_PICTURES}/$appName/Sessions"
    //private val sessionsDir = "Android/media/$appName/Sessions"

    fun insertImageFromFile(sessionId: String, file: File, displayName: String = defaultDisplayName()): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "$sessionsDir/$sessionId")
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { out ->
                    FileInputStream(file).use { input ->
                        input.copyTo(out)
                    }
                }
            }
            uri
        } else {
            // Fallback for pre-Android 10: write to public Pictures directory and scan
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val targetDir = File(picturesDir, "$appName/Sessions/$sessionId").apply { mkdirs() }
            val targetFile = File(targetDir, displayName)
            FileInputStream(file).use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }
            MediaScannerConnection.scanFile(context, arrayOf(targetFile.absolutePath), arrayOf("image/jpeg"), null)
            Uri.fromFile(targetFile)
        }
    }

    fun getImagesForSession(sessionId: String): List<Uri> {
        val result = mutableListOf<Uri>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.RELATIVE_PATH
            )
            val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
            val selectionArgs = arrayOf("$sessionsDir/$sessionId%")
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                    result.add(contentUri)
                }
            }
        } else {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val targetDir = File(picturesDir, "$appName/Sessions/$sessionId")
            if (targetDir.exists()) {
                targetDir.listFiles()?.sortedByDescending { it.lastModified() }?.forEach { file ->
                    result.add(Uri.fromFile(file))
                }
            }
        }
        return result
    }

    private fun defaultDisplayName(): String = "IMG_${System.currentTimeMillis()}.jpg"
}


