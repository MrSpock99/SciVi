package itis.ru.scivi.utils

object Const {
    object Article {
        const val ID: String = "id"
        const val PHOTOS: String = "photos"
        const val ATTACHMENTS: String = "attachments"
        const val NAME = "name"
        const val ARTICLES = "articles"
    }

    object FileType {
        const val IMAGE = "images"
        const val VIDEO = "videos"
    }

    object RequestCode {
        val ATTACHMENT_NAME: Int = 2
        val GALLERY: Int = 1
    }

    object Args {

        val ATTACHMENT: String = "ATTACHEMNT"
        val FILE_PATH: String = "FILE_PATH"
        val CREATE_ARTICLE: String = "createArticle"
    }
}
