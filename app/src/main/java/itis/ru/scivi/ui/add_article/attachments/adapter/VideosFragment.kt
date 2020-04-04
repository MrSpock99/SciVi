package itis.ru.scivi.ui.add_article.attachments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.scivi.R
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.Const

class VideosFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attachments, container, false)
    }

    companion object {
        fun newInstance(articleId: String): VideosFragment {
            val bundle = Bundle()
            bundle.putString(Const.Article.ID, articleId)
            val fragment = VideosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}