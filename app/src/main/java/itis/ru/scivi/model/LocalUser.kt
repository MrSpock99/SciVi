package itis.ru.scivi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocalUser(val email: String, val name: String) : Parcelable