package itis.ru.scivi.ui.article.attachments.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import itis.ru.scivi.R
import itis.ru.scivi.model.QrCodeModel
import itis.ru.scivi.ui.main.MainActivity
import itis.ru.scivi.utils.generateQrCode
import itis.ru.scivi.utils.saveTempBitmap
import itis.ru.scivi.workers.DeleteWorker
import itis.ru.scivi.workers.UploadWorker

interface AttachmentFragment {
    fun generateAndSaveQrCode(qrCodeModel: QrCodeModel, articleName: String) {
        generateQrCode(Gson().toJson(qrCodeModel))?.let { bitmap ->
            saveTempBitmap(
                bitmap = bitmap,
                imageName = qrCodeModel.name,
                articleName = articleName
            )
        }
    }


    fun observeUploadStatus(owner: LifecycleOwner) {
        WorkManager.getInstance().getWorkInfosByTagLiveData(UploadWorker::class.toString())
            .observe(owner, Observer { workInfo ->
                if (workInfo != null) {
                    if (workInfo.any { it.state == WorkInfo.State.SUCCEEDED }) {
                        getAttachments()
                        if (workInfo.size == 1) {
                            WorkManager.getInstance().pruneWork()
                        }
                    } else if (workInfo.all { it.state == WorkInfo.State.SUCCEEDED }) {
                        WorkManager.getInstance().pruneWork()
                    }
                }
            })
    }

    fun observeDeleteStatus(owner: LifecycleOwner, activity: MainActivity) {
        WorkManager.getInstance().getWorkInfosByTagLiveData(DeleteWorker::class.toString())
            .observe(activity, Observer { workInfo ->
                if (workInfo != null) {
                    if (workInfo.any { it.state == WorkInfo.State.SUCCEEDED }) {
                        getAttachments()
                        activity.showToastOneTime(activity.getString(R.string.deleted_successfully))
                        if (workInfo.size == 1) {
                            WorkManager.getInstance().pruneWork()
                        }
                    } else if (workInfo.all { it.state == WorkInfo.State.SUCCEEDED }) {
                        WorkManager.getInstance().pruneWork()
                    }
                }
            })
    }

    fun getAttachments()

    fun saveQrCodes()

    fun setVisibilities()
}