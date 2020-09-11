package itis.ru.scivi.model

data class ArticleRemote(
    val id: String = "", val name: String, val photoCount: Int = 0,
    val videoCount: Int = 0,
    val ARCount: Int = 0,
    val owner: RemoteUser
) {
    constructor() : this("", "", owner = RemoteUser("", ""))
    constructor(articleLocal: ArticleLocal) : this(
        articleLocal.id,
        articleLocal.name,
        articleLocal.photoCount,
        articleLocal.videoCount,
        articleLocal.ARCount,
        RemoteUser(articleLocal.owner.email, articleLocal.owner.name)
    )
}