package itis.ru.scivi.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.scivi.model.LocalUser

interface UserRepository {
    fun login(account: GoogleSignInAccount): Completable
    fun addUserToDb(localUser: LocalUser): Completable
    fun getUserFromDb(firebaseUser: FirebaseUser): Single<LocalUser>
    fun getMyProfile(): Single<LocalUser>
}
