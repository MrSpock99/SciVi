package itis.ru.scivi

import android.app.Application
import com.google.firebase.FirebaseApp
import org.kodein.di.*

class MyApp : Application(), KodeinAware {
    override lateinit var kodein: Kodein

    override val kodeinContext: KodeinContext<*>
        get() = AnyKodeinContext

    override val kodeinTrigger: KodeinTrigger?
        get() = KodeinTrigger()

    override fun onCreate() {
        super.onCreate()
        app = this
        kodein = itis.ru.scivi.di.Kodein().initKodein(this)
    }

    companion object{
        lateinit var app: MyApp
    }
}