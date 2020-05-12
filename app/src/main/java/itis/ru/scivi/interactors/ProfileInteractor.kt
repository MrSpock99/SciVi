package itis.ru.scivi.interactors

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.scivi.model.LocalUser
import itis.ru.scivi.repository.UserRepository

class ProfileInteractor constructor(
    private val userRepository: UserRepository
) {
    fun getUserInfo(firebaseUser: FirebaseUser): Single<LocalUser> {
        return userRepository.getUserFromDb(firebaseUser)
            .subscribeOn(Schedulers.io())
    }

    fun getMyProfile(): Single<LocalUser> {
        return userRepository.getMyProfile()
            .subscribeOn(Schedulers.io())
    }

    fun editUserInfo(localUser: LocalUser): Completable {
        return userRepository.addUserToDb(localUser)
            .subscribeOn(Schedulers.io())
    }
}
