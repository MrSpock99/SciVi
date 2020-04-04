package itis.ru.scivi.interactors

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.Photo
import itis.ru.scivi.repository.ArticleRepository

class ArticleInteractor(private val articleRepository: ArticleRepository) {
    fun addArticleToRemoteDb(articleRemote: ArticleRemote): Completable {
        return articleRepository.addArticleToRemoteDb(articleRemote)
            .subscribeOn(Schedulers.io())
    }

    fun getArticlePhotos(articleId: String): Single<List<Photo>> {
        return articleRepository.getArticlePhotos(articleId)
            .subscribeOn(Schedulers.io())
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
}