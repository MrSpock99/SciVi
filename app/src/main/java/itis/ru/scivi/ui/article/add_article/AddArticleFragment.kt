package itis.ru.scivi.ui.article.add_article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.getUser
import kotlinx.android.synthetic.main.fragment_add_article.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*

class AddArticleFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        btn_close.setOnClickListener {
            activity?.onBackPressed()
        }
        btn_continue.setOnClickListener {
            if (et_article_name.text.toString().isNotEmpty()) {
                val article = ArticleLocal(
                    name = et_article_name.text.toString(),
                    id = UUID.randomUUID().toString(),
                    owner = getUser()
                )
                val action =
                    AddArticleFragmentDirections.actionAddArticleFragmentToAddAttachmentsFragment(
                        article, true, user = getUser()
                    )
                rootActivity.navController.navigate(action)
            } else {
                rootActivity.alert(getString(R.string.emtpy_name), getString(R.string.error)) {
                    yesButton {}
                }.show()
            }
        }
    }
}