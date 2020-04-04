package itis.ru.scivi.ui.search

import androidx.lifecycle.MutableLiveData
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.ui.base.BaseViewModel
import itis.ru.scivi.utils.Response

class SearchViewModel(private val articleInteractor: ArticleInteractor) : BaseViewModel() {
    val articlesLiveData = MutableLiveData<Response<List<ArticleLocal>>>()

    fun getArticlesByKeyword(keyword: String) {
        disposables.add(
            articleInteractor
                .getAllArticlesByKeyword(keyword)
                .subscribe({
                    articlesLiveData.value = Response.success(it)
                }, {
                    articlesLiveData.value = Response.error(it)
                })
        )
    }
}