package itis.ru.scivi.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoLocal(
    val url: Uri?,
    var name: String = "",
    var upload: Boolean = false,
    var isSent: Boolean = true
) : Parcelable