package itis.ru.scivi.di

import itis.ru.scivi.repository.ArticleRepository
import itis.ru.scivi.repository.ArticleRepositoryImpl
import itis.ru.scivi.repository.UserRepository
import itis.ru.scivi.repository.UserRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun repositoryModule() = Kodein.Module("repositoryModule") {
    bind<UserRepository>() with singleton {
        UserRepositoryImpl(firebaseAuth = instance(), db = instance())
    }
    bind<ArticleRepository>() with singleton {
        ArticleRepositoryImpl(db = instance(),storage = instance())
    }
}