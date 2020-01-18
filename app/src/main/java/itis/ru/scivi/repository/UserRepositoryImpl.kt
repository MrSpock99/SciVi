package itis.ru.scivi.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.scivi.model.User

const val UID = "uid"
const val USER_NAME = "name"
const val USERS = "users"

class UserRepositoryImpl constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {
    override fun login(account: GoogleSignInAccount): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithCredential(
                GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(task.exception ?: Exception(""))
                }
            }
        }
    }

    override fun addUserToDb(user: User): Completable {
        return Completable.create { emitter ->
            val userMap = HashMap<String, Any>()
            userMap[UID] = firebaseAuth.currentUser?.email as String
            userMap[USER_NAME] = user.name
            db.collection(USERS)
                .document(firebaseAuth.currentUser?.email ?: "")
                .set(userMap)
                .addOnSuccessListener {
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                    Log.d("MYLOG", it.message)
                }
        }
    }

    override fun getUserFromDb(firebaseUser: FirebaseUser): Single<User> {
        return Single.create { emitter ->
            db.collection(USERS)
                .document(firebaseUser.email ?: "")
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        val user = result?.toObject(User::class.java)
                        if (user != null) {
                            emitter.onSuccess(user)
                        } else {
                            emitter.onError(
                                task.exception ?: Exception("error getting user from db")
                            )
                        }
                    } else {
                        emitter.onError(task.exception ?: Exception("error getting user from db"))
                    }
                }
        }
    }

    override fun getMyProfile(): Single<User> {
        return firebaseAuth.currentUser?.let {
            getUserFromDb(it)
        } ?: Single.error(Exception("user not exists"))
    }
}
