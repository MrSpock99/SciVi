package itis.ru.scivi.ui.article.attachments.adapter

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.stfalcon.imageviewer.StfalconImageViewer
import itis.ru.scivi.model.QrCodeModel
import itis.ru.scivi.ui.article.attachments.adapter.videos.VideoPlayerActivity
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.generateQrCode
import itis.ru.scivi.utils.saveTempBitmap

interface AttachmentFragment {
    fun saveQrCodes()
    fun openAttachment(context: Context, json: String) {
        val qrCodeModel = Gson().fromJson(json, QrCodeModel::class.java)
        if (qrCodeModel.fileType == Const.FileType.VIDEO) {
            context.startActivity(
                VideoPlayerActivity.newIntent(
                    context,
                    Uri.parse(qrCodeModel.url)
                )
            )
        } else if (qrCodeModel.fileType == Const.FileType.IMAGE) {
            StfalconImageViewer.Builder<QrCodeModel>(
                context,
                arrayListOf(qrCodeModel)
            ) { view, image ->
                Glide.with(context).load(image.url).into(view)
            }.show()
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