package itis.ru.scivi.ui.add_article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_article.*

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
            val article = ArticleLocal(name = et_article_name.text.toString())
            val action =
                AddArticleFragmentDirections.actionAddArticleFragmentToAddAttachmentsFragment(
                    article
                )
            rootActivity.navController.navigate(action)
        }
    }
}