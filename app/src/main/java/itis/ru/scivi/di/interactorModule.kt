package itis.ru.scivi.di

import itis.ru.scivi.interactors.AddArticleInteractor
import itis.ru.scivi.interactors.LoginInteractor
import itis.ru.scivi.interactors.ProfileInteractor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun interactorModule() = Kodein.Module("interactorModule") {
    bind<LoginInteractor>() with singleton {
        LoginInteractor(userRepository = instance())
    }
    bind<ProfileInteractor>() with singleton {
        ProfileInteractor(userRepository = instance())
    }
    bind<AddArticleInteractor>() with singleton {
        AddArticleInteractor(articleRepository = instance())
    }
}