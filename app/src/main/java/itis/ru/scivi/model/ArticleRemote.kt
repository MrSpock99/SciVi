package itis.ru.scivi.model

data class ArticleRemote(
    val id: String = "", val name: String, val photoCount: Int = 0,
    val videoCount: Int = 0,
    val ARCount: Int = 0
) {
    constructor() : this("", "")
}