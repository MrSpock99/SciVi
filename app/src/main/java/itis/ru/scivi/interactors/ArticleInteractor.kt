package itis.ru.scivi.interactors

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.PhotoLocal
import itis.ru.scivi.repository.ArticleRepository
import java.io.FileInputStream

class ArticleInteractor(private val articleRepository: ArticleRepository) {
    fun addArticleToRemoteDb(articleRemote: ArticleRemote): Completable {
        return articleRepository.addArticleToRemoteDb(articleRemote)
            .subscribeOn(Schedulers.io())
    }

    fun getArticlePhotos(articleId: String): Observable<List<PhotoLocal>> {
        /*return articleRepository.getArticlePhotos(articleId)
            .subscribeOn(Schedulers.io())
            .flatMap { list ->
                Observable.fromIterable(list)
                    .map { item ->
                        val photo = PhotoLocal(url = item.url)
                        photo.name = item.name
                        photo.miniatureUrl = item.miniatureUrl
                        return@map photo
                    }
                    .toList()
                    .toObservable()
            }*/
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

    fun getAllArticlesByKeyword(keyword: String): Observable<MutableList<ArticleLocal>> {
        return articleRepository.getArticlesByKeyword(keyword)
            .subscribeOn(Schedulers.io())
            .flatMap { list ->
                Observable.fromIterable(list)
                    .map { item -> ArticleLocal(id = item.id, name = item.name) }
                    .toList()
                    .toObservable()
            }
    }

    fun uploadFileFromStream(
        stream: FileInputStream,
        articleId: String,
        fileType: String,
        fileName: String
    ): Completable {
        return Completable.create { emitter ->
            val storage = Firebase.storage
            var storageRef = storage.reference
            var imagesRef: StorageReference? =
                storageRef.child("${articleId}/${fileType}/${fileName}")
            val uploadTask = imagesRef?.putStream(stream)
            uploadTask?.addOnSuccessListener {
                emitter.onComplete()
            }?.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }
}