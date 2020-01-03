package itis.ru.scivi.interactors

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.scivi.model.User
import itis.ru.scivi.repository.UserRepository

class ProfileInteractor constructor(
    private val userRepository: UserRepository
) {
    fun getUserInfo(firebaseUser: FirebaseUser): Single<User> {
        return userRepository.getUserFromDb(firebaseUser)
            .subscribeOn(Schedulers.io())
    }

    fun getMyProfile(): Single<User> {
        return userRepository.getMyProfile()
            .subscribeOn(Schedulers.io())
    }

    fun editUserInfo(user: User): Completable {
        return userRepository.addUserToDb(user)
            .subscribeOn(Schedulers.io())
    }
}
