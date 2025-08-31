package com.agcoding.oral.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agcoding.oral.di.AppContainer
import com.agcoding.oral.repository.SessionRepository
import com.agcoding.oral.utils.MediaStoreStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class CaptureViewModel(
    private val appContext: Context = AppContainer.appContext,
    private val repository: SessionRepository = AppContainer.sessionRepository,
    private val storage: MediaStoreStorage = AppContainer.mediaStoreStorage
) : ViewModel() {

    private var tempDir: File? = null

    private val _tempFiles = MutableStateFlow<List<File>>(emptyList())

    private val _finalizedUris = MutableStateFlow<List<Uri>>(emptyList())

    fun startNewCaptureSession() {
        val dir = File(appContext.cacheDir, "sessions/${UUID.randomUUID()}").apply { mkdirs() }
        tempDir = dir
        _tempFiles.value = emptyList()
        _finalizedUris.value = emptyList()
    }

    fun createTempFile(): File {
        val dir = tempDir ?: File(appContext.cacheDir, "sessions/${UUID.randomUUID()}").also { it.mkdirs(); tempDir = it }
        return File(dir, "IMG_${System.currentTimeMillis()}.jpg")
    }

    fun onTempImageSaved(file: File) {
        _tempFiles.value = _tempFiles.value + file
    }

    fun finalizeSession(sessionId: String, name: String, age: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            val uris = mutableListOf<Uri>()
            _tempFiles.value.forEach { file ->
                storage.insertImageFromFile(sessionId, file)?.let { uris.add(it) }
                file.delete()
            }
            repository.saveSession(sessionId = sessionId, name = name, age = age)
            _finalizedUris.value = uris
            _tempFiles.value = emptyList()
            tempDir?.deleteRecursively()
            tempDir = null
            onDone()
        }
    }
}