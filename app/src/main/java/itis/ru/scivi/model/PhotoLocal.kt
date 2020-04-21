package itis.ru.scivi.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoLocal(val url: Uri?, var name: String = "", var upload: Boolean = false): Parcelable
