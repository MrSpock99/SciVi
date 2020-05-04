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
        val QR_CODE: Int = 3
        val ATTACHMENT_NAME: Int = 2
        val GALLERY: Int = 1
    }

    object Args {
        val KEY_QR_CODE: String = "KEY_QR_CODE"
        val FILE_TYPE: String = "FILE_TYPE"
        val UPLOAD_MODEL: String = "UPLOAD_MODEL"
        val ATTACHMENT: String = "ATTACHEMNT"
        val FILE_PATH: String = "FILE_PATH"
        val CREATE_ARTICLE: String = "createArticle"
    }

    object VideoPlayerConfig {
        const val MIN_BUFFER_DURATION = 3000
        //Max Video you want to buffer during PlayBack
        const val MAX_BUFFER_DURATION = 5000
        //Min Video you want to buffer before start Playing it
        const val MIN_PLAYBACK_START_BUFFER = 1500
        //Min video You want to buffer when user resumes video
        const val MIN_PLAYBACK_RESUME_BUFFER = 5000
    }
}
