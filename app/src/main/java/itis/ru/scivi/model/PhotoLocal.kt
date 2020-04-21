package itis.ru.scivi.model

import android.net.Uri
import com.google.firebase.storage.StorageReference
import java.net.URL

data class PhotoLocal(val url: Uri?): Attachment()
