package itis.ru.scivi.interactors

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.repository.ArticleRepository

class AddArticleInteractor(private val articleRepository: ArticleRepository) {
    fun addArticleToRemoteDb(articleRemote: ArticleRemote): Completable {
        return articleRepository.addArticleToRemoteDb(articleRemote)
            .subscribeOn(Schedulers.io())
    }
}