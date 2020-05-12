package itis.ru.scivi.ui.article

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.RemoteUser
import itis.ru.scivi.ui.base.BaseViewModel
import itis.ru.scivi.utils.Response

class AddArticleViewModel(private val articleInteractor: ArticleInteractor) :
    BaseViewModel() {

    val addArticleLiveData = MutableLiveData<Response<Boolean>>()
    fun addArticleToDb(article: ArticleLocal) {
        disposables.add(
            articleInteractor
                .addArticleToRemoteDb(
                    ArticleRemote(
                        name = article.name,
                        id = article.id,
                        owner = RemoteUser(article.owner.email, article.owner.name)
                    )
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addArticleLiveData.value = Response.success(true)
                }, {
                    addArticleLiveData.value = Response.error(it)
                })
        )
    }
}