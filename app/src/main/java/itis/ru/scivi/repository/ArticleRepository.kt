package itis.ru.scivi.repository

import io.reactivex.Completable
import itis.ru.scivi.model.ArticleRemote

interface ArticleRepository {
    fun addArticleToRemoteDb(article: ArticleRemote): Completable
}