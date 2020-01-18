package itis.ru.scivi.di

import android.app.Application
import org.kodein.di.Kodein

class Kodein() {

    lateinit var di: Kodein

    fun initKodein(app: Application): Kodein {
        di = Kodein {
            import(appModule(app))
            import(authModule(app))
            import(viewModelModule())
            import(databaseModule(app))
            import(repositoryModule())
            import(interactorModule())
        }
        return di
    }
}