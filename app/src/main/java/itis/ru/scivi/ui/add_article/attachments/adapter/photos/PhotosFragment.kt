package itis.ru.scivi.ui.add_article.attachments.adapter.photos

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import itis.ru.scivi.R
import itis.ru.scivi.model.PhotoLocal
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.dpToPx
import kotlinx.android.synthetic.main.fragment_attachments.*
import org.kodein.di.generic.instance
import java.util.*

class PhotosFragment : BaseFragment() {
    private lateinit var adapter: PhotosAdapter
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: PhotosViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(PhotosViewModel::class.java)
    }
    private lateinit var articleId: String
    private var createArticle: Boolean = false
    var uploadItem: PhotoLocal? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleId = arguments?.getString(Const.Article.ID).toString()
        createArticle = arguments!!.getBoolean(Const.Args.CREATE_ARTICLE)
        return inflater.inflate(R.layout.fragment_attachments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        observePhotos()
        observeLoading()
        observeUploadStatus()
        viewModel.getArticlePhotos(arguments?.getString(Const.Article.ID).toString())
    }

    private fun observeLoading() {
        viewModel.showLoadingLiveData.observe(this, Observer {
            rootActivity.showLoading(it)
        })
    }

    private fun observeUploadStatus() {
        viewModel.uploadCompleteLiveData.observe(this, Observer {
            if (it.data != null)
                viewModel.getArticlePhotos(arguments?.getString(Const.Article.ID).toString())
        })
    }

    private fun observePhotos() {
        viewModel.photosLiveData.observe(this, Observer { response ->
            if (response.data != null) {
                if (response.data.isEmpty() && createArticle)
                    return@Observer
                uploadItem?.let {
                    response.data.add(it)
                }
                adapter.submitList(response.data)
            }
        })
    }

    private fun initRecycler() {
        val initList = mutableListOf<PhotoLocal>()
        if (createArticle) {
            uploadItem = PhotoLocal(null)
            uploadItem?.let {
                it.upload = true
                it.name = "Upload photo"
                initList.add(it)
            }
        }
        adapter =
            PhotosAdapter(
                initList,
                {
                    if (it.upload) {
                        startGalleryIntent()
                    }
                })
        adapter.submitList(initList)
        rv_attachments.adapter = adapter
        rv_attachments.layoutManager = GridLayoutManager(context, 2)
        val spacing = dpToPx(2)
        rv_attachments.setPadding(spacing, spacing, spacing, spacing)
        rv_attachments.clipToPadding = false
        rv_attachments.clipChildren = false
        rv_attachments.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })

    }

    private fun startGalleryIntent() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Choose image"),
            Const.RequestCode.GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.GALLERY && resultCode == Activity.RESULT_OK) {
            val uri: Uri?
            if (data != null && data.data != null) {
                uri = data.data!!
                viewModel.uploadFile(
                    uri,
                    articleId,
                    Const.FileType.IMAGE,
                    UUID.randomUUID().toString()
                )
            }
        }
    }

    companion object {
        fun newInstance(articleId: String, createArticle: Boolean): PhotosFragment {
            val bundle = Bundle()
            bundle.putString(Const.Article.ID, articleId)
            bundle.putBoolean(Const.Args.CREATE_ARTICLE, createArticle)
            val fragment =
                PhotosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}