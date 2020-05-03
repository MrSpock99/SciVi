package itis.ru.scivi.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import itis.ru.scivi.ui.add_article.AddArticleViewModel
import itis.ru.scivi.ui.add_article.attachments.adapter.photos.PhotosViewModel
import itis.ru.scivi.ui.login.LoginViewModel
import itis.ru.scivi.ui.main.MainViewModel
import itis.ru.scivi.ui.search.SearchViewModel
import itis.ru.scivi.utils.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun viewModelModule() = Kodein.Module(name = "viewModelModule") {
    bind<ViewModelProvider.Factory>() with singleton {
        ViewModelFactory(
            kodein.direct
        )
    }

    bind<ViewModel>(tag = MainViewModel::class.java.simpleName) with provider {
        MainViewModel(firebaseAuth = instance<FirebaseAuth>())
    }
    bind<ViewModel>(tag = LoginViewModel::class.java.simpleName) with provider {
        LoginViewModel(loginInteractor = instance(), profileInteractor = instance())
    }
    bind<ViewModel>(tag = AddArticleViewModel::class.java.simpleName) with provider {
        AddArticleViewModel(articleInteractor = instance())
    }
    bind<ViewModel>(tag = SearchViewModel::class.java.simpleName) with provider {
        SearchViewModel(articleInteractor = instance())
    }
    bind<ViewModel>(tag = PhotosViewModel::class.java.simpleName) with provider {
        PhotosViewModel(
            interactor = instance()
        )
    }
}