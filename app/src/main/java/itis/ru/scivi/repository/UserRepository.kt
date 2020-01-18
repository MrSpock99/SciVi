package itis.ru.scivi.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.scivi.model.User

interface UserRepository {
    fun login(account: GoogleSignInAccount): Completable
    fun addUserToDb(user: User): Completable
    fun getUserFromDb(firebaseUser: FirebaseUser): Single<User>
    fun getMyProfile(): Single<User>
}
