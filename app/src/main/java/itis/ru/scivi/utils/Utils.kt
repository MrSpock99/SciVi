package itis.ru.scivi.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import itis.ru.scivi.model.LocalUser
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun showKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun hideKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(
        InputMethodManager.HIDE_IMPLICIT_ONLY,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

fun getUser(): LocalUser {
    return LocalUser(
        FirebaseAuth.getInstance().currentUser?.email!!,
        FirebaseAuth.getInstance().currentUser?.displayName!!
    )
}

fun getStream(context: Context, uri: Uri): FileInputStream {
    val parcelFileDescriptor: InputStream? =
        context.getContentResolver().openInputStream(uri)

    return parcelFileDescriptor as FileInputStream
}

fun generateQrCode(text: String): Bitmap? {
    val hints = Hashtable<EncodeHintType, Any>()
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
    val multiFormatWriter = MultiFormatWriter()
    return try {
        val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200, hints)
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}

fun saveTempBitmap(bitmap: Bitmap, imageName: String, articleName: String): Boolean {
    return if (isExternalStorageWritable()) {
        saveImage(bitmap, imageName, articleName)
        true
    } else {
        false
    }
}

private fun saveImage(finalBitmap: Bitmap, imageName: String, articleName: String) {
    val root: String = Environment.getExternalStorageDirectory().toString()
    val myDir = File("$root/scivi/$articleName/qrcodes")
    myDir.mkdirs()
    //val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val fname = "$imageName.jpg"
    val file = File(myDir, fname)
    if (file.exists()) file.delete()
    try {
        val out = FileOutputStream(file)
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/* Checks if external storage is available for read and write */
fun isExternalStorageWritable(): Boolean {
    val state: String = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state
}
