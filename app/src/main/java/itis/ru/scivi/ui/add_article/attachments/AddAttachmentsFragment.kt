package itis.ru.scivi.ui.add_article.attachments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import itis.ru.scivi.R
import itis.ru.scivi.ui.add_article.attachments.adapter.AttachmentFragmentAdapter
import itis.ru.scivi.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_attachments.*

class AddAttachmentsFragment : BaseFragment() {
    val args: AddAttachmentsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_attachments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = AttachmentFragmentAdapter(fragmentManager!!)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
        setOnClickListeners()
        tv_article_name.text = args.argArticle.name
    }

    private fun setOnClickListeners(){
        btn_continue.setOnClickListener {

        }
    }
}