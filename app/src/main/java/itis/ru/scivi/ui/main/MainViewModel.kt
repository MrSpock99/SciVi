package itis.ru.scivi.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import itis.ru.scivi.MyApp
import itis.ru.scivi.ui.base.BaseViewModel
import itis.ru.scivi.utils.Response

class MainViewModel constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    val isLoginedLiveData = MutableLiveData<Response<Boolean>>()

    fun checkIsLogined() {
        if (firebaseAuth.currentUser != null) {
            isLoginedLiveData.value = Response.success(true)
        } else {
            isLoginedLiveData.value = Response.success(false)
        }
    }
}
