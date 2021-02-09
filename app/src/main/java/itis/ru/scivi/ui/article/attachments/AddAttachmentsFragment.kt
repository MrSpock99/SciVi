package itis.ru.scivi.ui.article.attachments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.ui.article.add_article.AddArticleViewModel
import itis.ru.scivi.ui.article.attachments.adapter.AttachmentFragmentAdapter
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.ui.main.MainActivity
import itis.ru.scivi.utils.getUser
import itis.ru.scivi.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_add_attachments.*
import org.jetbrains.anko.toast
import org.kodein.di.generic.instance

class AddAttachmentsFragment : BaseFragment() {
    private val args: AddAttachmentsFragmentArgs by navArgs()
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private lateinit var fragmentAdapter: AttachmentFragmentAdapter
    private val viewModel: AddArticleViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(AddArticleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_attachments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
        setOnClickListeners()
        setArticleInfo()
        setVisibilities()
        context?.let { hideKeyboard(it) }
        viewModel.addArticleLiveData.observe(this, Observer {
            rootActivity.toast(getString(R.string.article_add_success))
            rootActivity.navController.navigate(R.id.searchFragment)
        })
        MainActivity.shownToastCount = 0
    }

    private fun setVisibilities() {
        if (!args.createArticle && args.article.owner != getUser()) {
            btn_continue.visibility = View.GONE
        } else if (args.article.owner == getUser() && !args.createArticle) {
            btn_continue.visibility = View.VISIBLE
            btn_continue.text = getString(R.string.edit)
        } else if (args.createArticle && args.article.owner == getUser()) {
            btn_continue.visibility = View.VISIBLE
        }
    }

    private fun setOnClickListeners() {
        btn_continue.setOnClickListener {
            fragmentAdapter.saveQrCodes()
            if (args.createArticle) {
                viewModel.addArticleToDb(
                    ArticleLocal(
                        name = args.article.name,
                        id = args.article.id,
                        owner = getUser()
                    )
                )
            } else {
                rootActivity.toast(getString(R.string.article_edit_success))
                rootActivity.navController.navigate(R.id.searchFragment)
            }
        }
    }

    private fun setArticleInfo() {
        tv_article_name.text = args.article.name
    }

    private fun setViewPager() {
        fragmentAdapter =
            AttachmentFragmentAdapter(
                fragmentManager!!,
                args.article.id,
                args.createArticle,
                args.article.name,
                args.user,
                rootActivity
            )
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
    }

}