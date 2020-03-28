package itis.ru.scivi.ui.add_article

import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.scivi.interactors.AddArticleInteractor
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.ui.base.BaseViewModel

class AddArticleViewModel(private val addArticleInteractor: AddArticleInteractor) :
    BaseViewModel() {

    fun addArticleToDb(article: ArticleLocal) {
        disposables.add(addArticleInteractor
            .addArticleToRemoteDb(ArticleRemote(article.name))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {})
        )
    }
}