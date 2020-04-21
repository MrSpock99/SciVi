package itis.ru.scivi.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun databaseModule(context: Context) = Kodein.Module("databaseModule") {
    bind<FirebaseFirestore>() with singleton {
        FirebaseFirestore.getInstance()
    }
    bind<FirebaseStorage>() with singleton {
        Firebase.storage
    }
}