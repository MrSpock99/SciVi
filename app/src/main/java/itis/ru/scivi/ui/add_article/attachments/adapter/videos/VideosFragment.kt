package itis.ru.scivi.ui.add_article.attachments.adapter.videos

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
import androidx.work.WorkInfo
import androidx.work.WorkManager
import itis.ru.scivi.R
import itis.ru.scivi.model.VideoLocal
import itis.ru.scivi.ui.add_article.attachments.AttachmentNameActivity
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.dpToPx
import itis.ru.scivi.workers.UploadWorker
import kotlinx.android.synthetic.main.fragment_attachments.*
import org.kodein.di.generic.instance

class VideosFragment : BaseFragment() {
    private lateinit var adapter: VideosAdapter
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: VideosViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(VideosViewModel::class.java)
    }
    private lateinit var articleId: String
    private var createArticle: Boolean = false
    var uploadItem: VideoLocal? = null

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.getArticleVideos(arguments?.getString(Const.Article.ID).toString())
    }

    private fun observeUploadStatus() {
        WorkManager.getInstance().getWorkInfosByTagLiveData(UploadWorker::class.toString())
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    if (workInfo.any { it.state == WorkInfo.State.SUCCEEDED })
                        viewModel.getArticleVideos(arguments?.getString(Const.Article.ID).toString())
                }
            })
    }

    private fun observeLoading() {
        viewModel.showLoadingLiveData.observe(this, Observer {
            rootActivity.showLoading(it)
        })
    }

    private fun observePhotos() {
        viewModel.videosLiveData.observe(this, Observer { response ->
            if (response.data != null) {
                if (response.data.isEmpty() && createArticle)
                    return@Observer
                uploadItem?.let {
                    response.data.add(it)
                }
                val minusList = response.data.minus(adapter.list)
                val currentList = adapter.list
                minusList.forEach { videoLocal ->
                    currentList[currentList.indexOf(currentList.find { videoLocal.name == it.name })] =
                        videoLocal
                }
                adapter.submitList(currentList)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initRecycler() {
        val initList = mutableListOf<VideoLocal>()
        if (createArticle) {
            uploadItem = VideoLocal(null)
            uploadItem?.let {
                it.upload = true
                it.name = getString(R.string.upload_video)
                initList.add(it)
            }
        }
        adapter =
            VideosAdapter(
                initList
            ) {
                if (it.upload) {
                    startGalleryIntent()
                } else if (it.isSent) {
                    startActivity(VideoPlayerActivity.newIntent(rootActivity, it.url!!))
                }
            }
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
        intent.type = "video/*"
        startActivityForResult(
            Intent.createChooser(intent, "Choose video"),
            Const.RequestCode.GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.GALLERY && resultCode == Activity.RESULT_OK) {
            val uri: Uri?
            if (data != null && data.data != null) {
                uri = data.data!!
                startActivityForResult(
                    AttachmentNameActivity.newIntent(rootActivity, uri, Const.FileType.VIDEO),
                    Const.RequestCode.ATTACHMENT_NAME
                )
            }
        } else if (requestCode == Const.RequestCode.ATTACHMENT_NAME && resultCode == Activity.RESULT_OK) {
            val videoLocal = data!!.extras?.get(Const.Args.ATTACHMENT) as VideoLocal
            videoLocal.isSent = false
            adapter.list.add(0, videoLocal)
            adapter.notifyItemInserted(0)
            adapter.submitList(adapter.list)
            viewModel.uploadFile(
                videoLocal.url!!,
                articleId,
                Const.FileType.VIDEO,
                videoLocal.name
            )
        }
    }


    companion object {
        fun newInstance(articleId: String, createArticle: Boolean): VideosFragment {
            val bundle = Bundle()
            bundle.putString(Const.Article.ID, articleId)
            bundle.putBoolean(Const.Args.CREATE_ARTICLE, createArticle)
            val fragment =
                VideosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}