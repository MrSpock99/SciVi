package itis.ru.scivi

import io.reactivex.Observable
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.LocalUser
import itis.ru.scivi.ui.search.SearchViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class SearchViewModelTest : BaseTest() {
    private lateinit var viewModel: SearchViewModel

    @Mock
    private lateinit var articleInteractor: ArticleInteractor
    private var testName = "Test name"
    private var testResponse = mutableListOf(
        ArticleLocal(name = "Test", owner = mock(LocalUser::class.java)),
        ArticleLocal(name = "Test na", owner = mock(LocalUser::class.java))
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = SearchViewModel(articleInteractor)
    }

    @Test
    fun testGetArticlesByKeyword() {
        `when`(articleInteractor.getAllArticlesByKeyword(testName)).thenReturn(
            Observable.fromArray(
                testResponse
            )
        )
        viewModel.getArticlesByKeyword(testName)
        Assert.assertEquals(testResponse, viewModel.articlesLiveData.value?.data)
    }
}