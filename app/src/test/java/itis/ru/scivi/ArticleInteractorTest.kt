package itis.ru.scivi

import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.TestObserver
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.repository.ArticleRepository
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ArticleInteractorTest : BaseTest() {
    private lateinit var articleInteractor: ArticleInteractor

    @Mock
    private lateinit var articleRepository: ArticleRepository
    private var testArticle = Mockito.mock(ArticleRemote::class.java)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        articleInteractor = ArticleInteractor(articleRepository)
    }

    @Test
    fun testAddArticleToRemoteDb() {
        val repositoryAddArticleObserver: TestObserver<Completable> = TestObserver.create()
        val addArticleCompletable = Completable.complete()
            .doOnSubscribe { d: Disposable? ->
                repositoryAddArticleObserver.onSubscribe(
                    d!!
                )
            }
        //`when`(articleRepository.addArticleToRemoteDb(testArticle)).thenReturn(addArticleCompletable)
        given(articleRepository.addArticleToRemoteDb(testArticle)).willReturn(addArticleCompletable)
        //articleRepository.addArticleToRemoteDb(testArticle)
        repositoryAddArticleObserver.awaitTerminalEvent()
        repositoryAddArticleObserver.assertSubscribed()
    }
}