package itis.ru.scivi

import android.net.Uri
import io.reactivex.Observable
import itis.ru.scivi.interactors.ArticleInteractor
import itis.ru.scivi.model.PhotoLocal
import itis.ru.scivi.ui.article.attachments.adapter.photos.PhotosViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PhotosViewModelTest : BaseTest() {
    private lateinit var viewModel: PhotosViewModel

    @Mock
    private lateinit var articleInteractor: ArticleInteractor
    private var testArticleId = "007"
    private var testResponsePhotos: MutableList<PhotoLocal> =
        mutableListOf(PhotoLocal(Uri.parse("test1")), PhotoLocal(Uri.parse("test2")))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = PhotosViewModel(articleInteractor)
    }

    @Test
    fun testGetArticlePhotos() {
        `when`(articleInteractor.getArticlePhotos(testArticleId)).thenReturn(
            Observable.fromArray(
                testResponsePhotos
            )
        )
        viewModel.getArticlePhotos(testArticleId)
        Assert.assertEquals(testResponsePhotos, viewModel.photosLiveData.value?.data)
    }

}