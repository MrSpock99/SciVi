package itis.ru.scivi.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.Photo

interface ArticleRepository {
    fun addArticleToRemoteDb(article: ArticleRemote): Completable
    fun getArticlePhotos(articleId: String): Single<List<Photo>>
    fun getArticlesByKeyword(keyword: String): Observable<List<ArticleRemote>>
}