package itis.ru.scivi.ui.add_article.attachments.adapter.videos

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.VideoLocal
import itis.ru.scivi.ui.base.BaseViewModel
import itis.ru.scivi.utils.Response

class VideosViewModel(private val interactor: ArticleInteractor) : BaseViewModel() {

    val videosLiveData = MutableLiveData<Response<MutableList<VideoLocal>>>()

    fun getArticleVideos(articleId: String) {
        showLoadingLiveData.value = true

        disposables.add(
            interactor.getArticleVideos(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    videosLiveData.value = Response.success(it.toMutableList())
                    showLoadingLiveData.value = false
                }, {
                    videosLiveData.value = Response.error(it)
                    showLoadingLiveData.value = false
                })
        )
    }

    fun uploadFile(uri: Uri, articleId: String, fileType: String, name: String) {
        interactor.uploadFileFromUri(
            articleId = articleId, fileType = fileType, fileName = name, uri = uri
        )
    }
}