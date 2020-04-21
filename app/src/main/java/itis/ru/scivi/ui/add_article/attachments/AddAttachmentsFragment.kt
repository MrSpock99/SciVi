package itis.ru.scivi.ui.add_article.attachments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.ui.add_article.AddArticleViewModel
import itis.ru.scivi.ui.add_article.attachments.adapter.AttachmentFragmentAdapter
import itis.ru.scivi.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_attachments.*
import org.kodein.di.generic.instance

class AddAttachmentsFragment : BaseFragment() {
    private val args: AddAttachmentsFragmentArgs by navArgs()
    private val viewModeFactory: ViewModelProvider.Factory by instance()
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
    }

    private fun setOnClickListeners() {
        btn_continue.setOnClickListener {
            viewModel.addArticleToDb(ArticleLocal(name = args.article.name))
        }
    }

    private fun setArticleInfo() {
        tv_article_name.text = args.article.name
    }

    private fun setViewPager() {
        val fragmentAdapter = AttachmentFragmentAdapter(fragmentManager!!, args.article.id, args.createArticle)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
    }

}