package itis.ru.scivi.model

import android.net.Uri
import com.google.firebase.storage.StorageReference

data class PhotoRemote(val url: Uri): Attachment()