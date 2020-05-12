package itis.ru.scivi.repository

import io.reactivex.Completable
import io.reactivex.Observable
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.PhotoRemote
import itis.ru.scivi.model.VideoRemote

interface ArticleRepository {
    fun addArticleToRemoteDb(article: ArticleRemote): Completable
    fun getArticlePhotos(articleId: String): Observable<List<PhotoRemote>>
    fun getAllArticles(): Observable<List<ArticleRemote>>
    fun getArticleVideos(articleId: String): Observable<List<VideoRemote>>
}