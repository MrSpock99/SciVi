package itis.ru.scivi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleLocal(val id: String = "", val name: String) : Parcelable