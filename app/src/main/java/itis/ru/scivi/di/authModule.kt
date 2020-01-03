package itis.ru.scivi.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import itis.ru.scivi.R
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun authModule(context: Context) = Kodein.Module("authModule") {
    bind<GoogleSignInOptions>() with singleton {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.resources.getString(R.string.google_api_token))
            .requestEmail()
            .build()
    }

    bind<GoogleSignInClient>() with singleton {
        GoogleSignIn.getClient(context, instance())
    }
}