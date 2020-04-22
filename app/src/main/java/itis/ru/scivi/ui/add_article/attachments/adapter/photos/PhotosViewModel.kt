package itis.ru.scivi.ui.add_article.attachments.adapter.photos

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.StorageReference
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.PhotoLocal
import itis.ru.scivi.ui.base.BaseViewModel
import itis.ru.scivi.utils.Response
import itis.ru.scivi.utils.getStream

class PhotosViewModel(private val interactor: ArticleInteractor, private val context: Context) :
    BaseViewModel() {
    val photosLiveData = MutableLiveData<Response<MutableList<PhotoLocal>>>()
    val uploadCompleteLiveData = MutableLiveData<Response<Boolean>>()

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

    fun uploadFile(uri: Uri, articleId: String, fileType: String, name: String) {
        showLoadingLiveData.value = true

        disposables.add(
            interactor.uploadFileFromStream(
                stream = getStream(context = context, uri = uri),
                articleId = articleId,
                fileType = fileType,
                fileName = name
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uploadCompleteLiveData.value = Response.success(true)
                    showLoadingLiveData.value = false
                }, {
                    uploadCompleteLiveData.value = Response.error(it)
                    showLoadingLiveData.value = false
                })
        )
    }
}