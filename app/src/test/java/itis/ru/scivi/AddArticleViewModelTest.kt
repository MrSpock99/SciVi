package itis.ru.scivi

import io.reactivex.Completable
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.ArticleRemote
import itis.ru.scivi.model.LocalUser
import itis.ru.scivi.ui.article.AddArticleViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AddArticleViewModelTest : BaseTest() {
    private lateinit var viewModel: AddArticleViewModel

    @Mock
    private lateinit var articleInteractor: ArticleInteractor
    private var testLocalArticle: ArticleLocal = ArticleLocal(name = "", owner = LocalUser("", ""))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = AddArticleViewModel(articleInteractor)
    }

    @Test
    fun testAddArticleToRemoteDbSuccess() {
        `when`(articleInteractor.addArticleToRemoteDb(ArticleRemote(articleLocal = testLocalArticle))).thenReturn(
            Completable.complete()
        )
        viewModel.addArticleToDb(testLocalArticle)
        Assert.assertEquals(true, viewModel.addArticleLiveData.value?.data)
    }

    @Test(expected = Exception::class)
    fun testAddArticleToRemoteDbError() {
        `when`(articleInteractor.addArticleToRemoteDb(ArticleRemote(articleLocal = testLocalArticle))).thenThrow(
            Exception()
        )
        viewModel.addArticleToDb(testLocalArticle)
    }
}