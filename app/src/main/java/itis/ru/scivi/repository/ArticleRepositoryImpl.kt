package itis.ru.scivi.repository

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.utils.Const

class ArticleRepositoryImpl(private val db: FirebaseFirestore) : ArticleRepository {
    override fun addArticleToRemoteDb(article: ArticleRemote): Completable {
        return Completable.create { emitter ->
            val articleMap = HashMap<String, Any>()
            articleMap[Const.Article.NAME] = article.name
            db.collection(Const.Article.COLLECTION_NAME)
                .document()
                .set(articleMap)
                .addOnSuccessListener {
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}