package itis.ru.scivi.utils

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import java.io.FileInputStream
import java.io.InputStream

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun getStream(context: Context, uri: Uri): FileInputStream {
    val parcelFileDescriptor: InputStream? =
        context.getContentResolver().openInputStream(uri)

    return parcelFileDescriptor as FileInputStream
}
