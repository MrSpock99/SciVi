package itis.ru.scivi.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import itis.ru.scivi.R
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.LoginState
import itis.ru.scivi.utils.ScreenState
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.kodein.di.generic.instance

class LoginFragment : BaseFragment() {
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(LoginViewModel::class.java)
    }
    private val mGoogleApiClient: GoogleSignInClient by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        init(rootView)
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onGoogleIntentResult(requestCode, data)
    }

    private fun init(view: View) {
        viewModel.loginState.observe(::getLifecycle, ::updateUI)

        view.btn_login.setOnClickListener {
            openGoogleActivity(mGoogleApiClient)
        }
    }

    private fun updateUI(screenState: ScreenState<LoginState>?) {
        when (screenState) {
            ScreenState.Loading -> rootActivity.showLoading(true)
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: LoginState) {
        rootActivity.showLoading(false)
        when (renderState) {
            LoginState.SuccessLogin -> rootActivity.navController.navigate(R.id.action_loginFragment_to_searchFragment)

            LoginState.SuccessRegister -> rootActivity.navController.navigate(R.id.action_loginFragment_to_searchFragment)

           /* LoginState.Error -> view?.let {
                showSnackbar(getString(R.string.snackbar_error_message))
            }*/
        }
    }

    private fun openGoogleActivity(googleSignInClient: GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, LoginViewModel.REQUEST_AUTH)
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
