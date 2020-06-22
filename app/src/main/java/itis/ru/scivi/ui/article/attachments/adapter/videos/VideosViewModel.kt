package itis.ru.scivi.ui.article.attachments.adapter.videos

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.VideoLocal
import itis.ru.scivi.ui.article.attachments.adapter.base.AttachmentViewModel
import itis.ru.scivi.utils.Response

class VideosViewModel(private val interactor: ArticleInteractor) : AttachmentViewModel(interactor) {

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

}