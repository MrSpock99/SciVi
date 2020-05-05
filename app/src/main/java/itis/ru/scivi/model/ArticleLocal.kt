package itis.ru.scivi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleLocal(
    val id: String = "",
    val name: String,
    val photoCount: Int = 0,
    val videoCount: Int = 0,
    val ARCount: Int = 0
) : Parcelable