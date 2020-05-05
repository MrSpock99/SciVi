package itis.ru.scivi.ui.article.attachments.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.stfalcon.imageviewer.StfalconImageViewer
import itis.ru.scivi.R
import itis.ru.scivi.model.QrCodeModel
import itis.ru.scivi.ui.article.attachments.adapter.videos.VideoPlayerActivity
import itis.ru.scivi.ui.main.MainActivity
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.generateQrCode
import itis.ru.scivi.utils.saveTempBitmap
import org.jetbrains.anko.toast

interface AttachmentFragment {
    fun saveQrCodes()
    fun openAttachment(context: Context, json: String, fromSearchFragment: Boolean) {
        try {
            val qrCodeModel = Gson().fromJson(json, QrCodeModel::class.java)
            if (qrCodeModel.fileType == Const.FileType.VIDEO) {
                context.startActivity(
                    VideoPlayerActivity.newIntentWithQrCodeModel(
                        context,
                        qrCodeModel,
                        fromSearchFragment
                    )
                )
            } else if (qrCodeModel.fileType == Const.FileType.IMAGE) {
                StfalconImageViewer.Builder<QrCodeModel>(
                    context,
                    arrayListOf(qrCodeModel)
                ) { view, image ->
                    Glide.with(context).load(image.url).into(view)
                }.withDismissListener {
                    if (fromSearchFragment) {
                        context.startActivity(MainActivity.newIntent(context, qrCodeModel.article))
                    }
                }.show()
            }
        } catch (ex: JsonSyntaxException) {
            context.toast(context.getString(R.string.invalid_qrcode))
        }
    }

    fun generateAndSaveQrCode(qrCodeModel: QrCodeModel, articleName: String) {
        generateQrCode(Gson().toJson(qrCodeModel))?.let { bitmap ->
            saveTempBitmap(
                bitmap = bitmap,
                imageName = qrCodeModel.name,
                articleName = articleName
            )
        }
    }

    fun setVisibilities()
}