package itis.ru.scivi.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.PhotoRemote

interface ArticleRepository {
    fun addArticleToRemoteDb(article: ArticleRemote): Completable
    fun getArticlePhotos(articleId: String): Observable<List<PhotoRemote>>
    fun getArticlesByKeyword(keyword: String): Observable<List<ArticleRemote>>
}