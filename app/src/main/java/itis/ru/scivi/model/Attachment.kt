package itis.ru.scivi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Attachment: Parcelable{
    var miniatureUrl: String = ""
    var name: String = ""
    var upload: Boolean = false
}