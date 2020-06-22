package itis.ru.scivi.ui.article.attachments.adapter.base

import android.net.Uri
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.ui.base.BaseViewModel

open class AttachmentViewModel(private val interactor: ArticleInteractor) : BaseViewModel() {
    fun uploadFile(uri: Uri, articleId: String, fileType: String, name: String) {
        interactor.uploadFileFromUri(
            articleId = articleId, fileType = fileType, fileName = name, uri = uri
        )
    }

    fun deleteFile(articleId: String, fileType: String, name: String) {
        interactor.deleteFile(articleId = articleId, fileType = fileType, fileName = name)
    }
}