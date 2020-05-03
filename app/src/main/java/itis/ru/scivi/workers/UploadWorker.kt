package itis.ru.scivi.workers

import android.content.Context
import android.net.Uri
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import io.reactivex.Single
import itis.ru.scivi.model.UploadModel
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.UriDeserializer

class UploadWorker(private val context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {
    override fun createWork(): Single<Result> {
        return Single.create { emitter ->
            val gson = GsonBuilder()
                .registerTypeAdapter(Uri::class.java, UriDeserializer())
                .create()
            val data = gson.fromJson(
                inputData.getString(Const.Args.UPLOAD_MODEL),
                UploadModel::class.java
            )
            val storage = Firebase.storage
            var storageRef = storage.reference
            var imagesRef: StorageReference? =
                storageRef.child(
                    "${data.articleId}/${data.fileType}/${data.name}"
                )
            val uploadTask = imagesRef?.putFile(data.uri)
            uploadTask?.addOnSuccessListener {
                emitter.onSuccess(Result.success())
            }?.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }
}