package itis.ru.scivi.model

import android.net.Uri
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoRemote(val url: Uri): Attachment()