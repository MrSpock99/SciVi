package itis.ru.scivi.ui.add_article.attachments.adapter

import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.ui.base.BaseViewModel

class PhotosViewModel(private val interactor: ArticleInteractor) : BaseViewModel() {
    fun getArticlePhotos(articleId: String) {
        disposables.add(
            interactor.getArticlePhotos(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {

                })
        )
    }
}