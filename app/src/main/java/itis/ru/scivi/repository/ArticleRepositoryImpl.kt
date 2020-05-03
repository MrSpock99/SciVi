package itis.ru.scivi.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Observable
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.PhotoRemote
import itis.ru.scivi.model.VideoRemote
import itis.ru.scivi.utils.Const
import org.jetbrains.anko.doAsync

class ArticleRepositoryImpl(private val db: FirebaseFirestore, private val storage: FirebaseStorage) : ArticleRepository {
    override fun addArticleToRemoteDb(article: ArticleRemote): Completable {
        return Completable.create { emitter ->
            val articleMap = HashMap<String, Any>()
            articleMap[Const.Article.NAME] = article.name
            val document = db.collection(Const.Article.ARTICLES)
                .document()
            articleMap[Const.Article.ID] = article.id

            document.set(articleMap)
                .addOnSuccessListener {
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun getArticlePhotos(articleId: String): Observable<List<PhotoRemote>> {
        return Observable.create {emitter ->
            val imageRef = storage.reference.child("${articleId}/${Const.FileType.IMAGE}/")
            imageRef.listAll().addOnSuccessListener {
                val list = mutableListOf<PhotoRemote>()
                doAsync {
                    it.items.forEach { ref ->
                        val uri = Tasks.await(ref.downloadUrl.addOnCompleteListener { task ->
                        })
                        val photoRemote = PhotoRemote(url = uri)
                        photoRemote.name = ref.name
                        list.add(photoRemote)
                    }
                    emitter.onNext(list)
                }.get()
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

    override fun getArticleVideos(articleId: String): Observable<List<VideoRemote>> {
        return Observable.create { emitter ->
            val imageRef = storage.reference.child("${articleId}/${Const.FileType.VIDEO}/")
            imageRef.listAll().addOnSuccessListener {
                val list = mutableListOf<VideoRemote>()
                doAsync {
                    it.items.forEach { ref ->
                        val uri = Tasks.await(ref.downloadUrl.addOnCompleteListener { task ->
                        })
                        val videoRemote = VideoRemote(url = uri)
                        videoRemote.name = ref.name
                        list.add(videoRemote)
                    }
                    emitter.onNext(list)
                }.get()
            }
        }
    }
}
