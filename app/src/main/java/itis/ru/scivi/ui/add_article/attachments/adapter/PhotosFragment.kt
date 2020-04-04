package itis.ru.scivi.ui.add_article.attachments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import itis.ru.scivi.R
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.Const
import kotlinx.android.synthetic.main.fragment_attachments.*
import org.kodein.di.generic.instance

class PhotosFragment : BaseFragment() {
    private lateinit var adapter: PhotosAdapter
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: PhotosViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(PhotosViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attachments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_attachments.layoutManager = LinearLayoutManager(context)
        adapter = PhotosAdapter(mutableListOf(), {

        })
        viewModel.getArticlePhotos(arguments?.getString(Const.Article.ID).toString())
    }

    companion object {
        fun newInstance(articleId: String): PhotosFragment {
            val bundle = Bundle()
            bundle.putString(Const.Article.ID, articleId)
            val fragment = PhotosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}