package itis.ru.scivi.interactors

import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import itis.ru.scivi.model.*
import itis.ru.scivi.repository.ArticleRepository
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.UriSerializer
import itis.ru.scivi.workers.DeleteWorker
import itis.ru.scivi.workers.UploadWorker

class ArticleInteractor(private val articleRepository: ArticleRepository) {
    fun addArticleToRemoteDb(articleRemote: ArticleRemote): Completable {
        return articleRepository.addArticleToRemoteDb(articleRemote)
            .subscribeOn(Schedulers.io())
    }

    fun getArticlePhotos(articleId: String): Observable<List<PhotoLocal>> {
        return articleRepository.getArticlePhotos(articleId)
            .subscribeOn(Schedulers.io())
            .flatMap { list ->
                Observable.fromIterable(list)
                    .map { item ->
                        return@map PhotoLocal(url = item.url, name = item.name)
                    }
                    .toList()
                    .toObservable()
            }
    }

    fun getArticleVideos(articleId: String): Observable<List<VideoLocal>> {
        return articleRepository.getArticleVideos(articleId).subscribeOn(Schedulers.io())
            .flatMap { list ->
                Observable.fromIterable(list)
                    .map { item ->
                        return@map VideoLocal(url = item.url, name = item.name)
                    }
                    .toList()
                    .toObservable()
            }
    }

    fun getAllArticlesByKeyword(keyword: String): Observable<MutableList<ArticleLocal>> {
        return articleRepository.getAllArticles()
            .subscribeOn(Schedulers.io())
            .flatMap { list ->
                Observable.fromIterable(list)
                    .filter { item -> item.name.contains(keyword) }
                    .map { item ->
                        ArticleLocal(
                            id = item.id,
                            name = item.name,
                            owner = LocalUser(item.owner.email, item.owner.name)
                        )
                    }
                    .toList()
                    .toObservable()
            }
    }

    fun uploadFileFromUri(
        articleId: String,
        fileType: String,
        fileName: String,
        uri: Uri
    ) {
        val gson = GsonBuilder()
            .registerTypeAdapter(Uri::class.java, UriSerializer())
            .create()
        val uploadModel =
            UploadModel(uri = uri, articleId = articleId, fileType = fileType, name = fileName)
        val workData = Data.Builder().putString(
            Const.Args.UPLOAD_MODEL, gson.toJson(uploadModel)
        ).build()
        val uploadWorkRequest =
            OneTimeWorkRequest.Builder(UploadWorker::class.java)
                .setInputData(workData)
                .addTag(UploadWorker::class.toString())
                .build()
        WorkManager.getInstance().enqueue(uploadWorkRequest)
    }

    fun deleteFile(
        articleId: String,
        fileType: String,
        fileName: String
    ) {
        val gson = GsonBuilder()
            .create()
        val deleteModel =
            DeleteModel(articleId = articleId, fileType = fileType, name = fileName)
        val workData = Data.Builder().putString(
            Const.Args.DELETE_MODEL, gson.toJson(deleteModel)
        ).build()
        val deleteWorkRequest =
            OneTimeWorkRequest.Builder(DeleteWorker::class.java)
                .setInputData(workData)
                .addTag(DeleteWorker::class.toString())
                .build()
        WorkManager.getInstance().enqueue(deleteWorkRequest)
    }
}