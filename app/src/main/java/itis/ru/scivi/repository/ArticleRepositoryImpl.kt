package itis.ru.scivi.repository

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.Photo
import itis.ru.scivi.utils.Const

class ArticleRepositoryImpl(private val db: FirebaseFirestore) : ArticleRepository {
    override fun addArticleToRemoteDb(article: ArticleRemote): Completable {
        return Completable.create { emitter ->
            val articleMap = HashMap<String, Any>()
            articleMap[Const.Article.NAME] = article.name
            val document = db.collection(Const.Article.ARTICLES)
                .document()
            articleMap[Const.Article.ID] = document.id

            document.set(articleMap)
                .addOnSuccessListener {
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun getArticlePhotos(articleId: String): Single<List<Photo>> {
        return Single.create { emitter ->
            db.collection(Const.Article.ATTACHMENTS)
                .document(articleId).collection(Const.Article.PHOTOS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.toObjects(Photo::class.java)
                            ?.let { emitter.onSuccess(it) }
                    } else {
                        emitter.onError(
                            task.exception ?: java.lang.Exception("error getting all photos")
                        )
                    }
                }
        }
    }

    override fun getArticlesByKeyword(keyword: String): Observable<List<ArticleRemote>> {
        return Observable.create { emitter ->
            db.collection(Const.Article.ARTICLES)
                .whereGreaterThanOrEqualTo(Const.Article.NAME, keyword)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.toObjects(ArticleRemote::class.java)
                            ?.let { emitter.onNext(it) }
                    } else {
                        emitter.onError(
                            task.exception ?: java.lang.Exception(
                                "error getting articles by name"
                            )
                        )
                    }
                }
        }
    }
}
