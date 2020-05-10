package itis.ru.scivi.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import dmax.dialog.SpotsDialog
import itis.ru.scivi.MyApp
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.ui.search.SearchFragmentDirections
import itis.ru.scivi.utils.Const
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein =
        MyApp.app.kodein

    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(MainViewModel::class.java)
    }

    val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (intent.getParcelableExtra<ArticleLocal>(Const.Args.ARTICLE) != null) {
            navController.popBackStack(R.id.loginFragment, true)
            goToArticle(article = intent.getParcelableExtra(Const.Args.ARTICLE))
        } else {
            viewModel.checkIsLogined()
            observeIsLoginedLiveData()
        }
        dialog =
            SpotsDialog.Builder().setContext(this).setTheme(R.style.ProgressDialogTheme).build()
    }

    private fun goToArticle(article: ArticleLocal) {
        navController.navigate(R.id.searchFragment)
        val action = SearchFragmentDirections.actionSearchFragmentToAddAttachmentsFragment(
            article
        )
        navController.navigate(action)
    }

    fun showLoading(show: Boolean) {
        /*  val dialog = ProgressDialog.newInstance()
          try {
              if (show && !dialog.isVisible && !dialog.isAdded) {
                  dialog.show(supportFragmentManager, dialog.toString())
              } else {
                  if (dialog.isVisible)
                      dialog.dismiss()
              }
          }catch (ex: IllegalStateException){

          }*/

        if (show)
            dialog.show()
        else
            dialog.dismiss()
    }

    private fun observeIsLoginedLiveData() =
        viewModel.isLoginedLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                if (response.data) {
                    navController.navigate(R.id.action_loginFragment_to_searchFragment)
                } else {
                    navController.navigate(R.id.loginFragment)
                }
            }
        })

    companion object {
        fun newIntent(context: Context, article: ArticleLocal): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(Const.Args.ARTICLE, article)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }
}