package itis.ru.scivi.utils

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.FileInputStream
import java.io.InputStream

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun showKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun getStream(context: Context, uri: Uri): FileInputStream {
    val parcelFileDescriptor: InputStream? =
        context.getContentResolver().openInputStream(uri)

    return parcelFileDescriptor as FileInputStream
}
