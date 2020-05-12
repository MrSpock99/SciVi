package itis.ru.scivi.model

data class QrCodeModel(
    val url: String,
    val name: String,
    val fileType: String,
    val article: ArticleLocal,
    val owner: LocalUser
)