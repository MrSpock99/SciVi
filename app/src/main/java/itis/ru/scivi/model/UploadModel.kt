package itis.ru.scivi.model

import android.net.Uri

data class UploadModel(val uri: Uri, val articleId: String, val fileType: String, val name: String)