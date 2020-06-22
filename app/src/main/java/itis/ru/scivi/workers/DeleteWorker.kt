package itis.ru.scivi.workers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import io.reactivex.Single
import itis.ru.scivi.model.DeleteModel
import itis.ru.scivi.utils.Const

class DeleteWorker(private val context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {
    override fun createWork(): Single<Result> {
        return Single.create { emitter ->
            val gson = GsonBuilder()
                .create()
            val data = gson.fromJson(
                inputData.getString(Const.Args.DELETE_MODEL),
                DeleteModel::class.java
            )
            val storage = Firebase.storage
            var storageRef = storage.reference
            var imagesRef: StorageReference? =
                storageRef.child(
                    "${data.articleId}/${data.fileType}/${data.name}"
                )
            imagesRef?.delete()?.addOnSuccessListener {
                emitter.onSuccess(Result.success())
            }?.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }
}