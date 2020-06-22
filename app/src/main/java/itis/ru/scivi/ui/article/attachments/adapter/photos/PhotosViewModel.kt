package itis.ru.scivi.ui.article.attachments.adapter.photos

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.PhotoLocal
import itis.ru.scivi.ui.article.attachments.adapter.base.AttachmentViewModel
import itis.ru.scivi.utils.Response

class PhotosViewModel(private val interactor: ArticleInteractor) : AttachmentViewModel(interactor) {
    val photosLiveData = MutableLiveData<Response<MutableList<PhotoLocal>>>()

    fun getArticlePhotos(articleId: String) {
        showLoadingLiveData.value = true

        disposables.add(
            interactor.getArticlePhotos(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    photosLiveData.value = Response.success(it.toMutableList())
                    showLoadingLiveData.value = false
                }, {
                    photosLiveData.value = Response.error(it)
                    showLoadingLiveData.value = false
                })
        )
    }
}