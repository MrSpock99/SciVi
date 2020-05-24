package itis.ru.scivi.ui.article.attachments.adapter.videos

import android.Manifest
import android.annotation.SuppressLint
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
import com.tbruyelle.rxpermissions2.RxPermissions
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.LocalUser
import itis.ru.scivi.model.QrCodeModel
import itis.ru.scivi.model.VideoLocal
import itis.ru.scivi.ui.article.QrCodeScanner
import itis.ru.scivi.ui.article.attachments.AttachmentNameActivity
import itis.ru.scivi.ui.article.attachments.adapter.AttachmentFragment
import itis.ru.scivi.ui.article.attachments.adapter.OpenAttachment
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.dpToPx
import itis.ru.scivi.utils.getUser
import kotlinx.android.synthetic.main.fragment_attachments.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import org.kodein.di.generic.instance

class VideosFragment : BaseFragment(), AttachmentFragment, OpenAttachment {
    private lateinit var adapter: VideosAdapter
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: VideosViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(VideosViewModel::class.java)
    }
    private lateinit var articleId: String
    private lateinit var articleName: String
    private lateinit var owner: LocalUser
    private var createArticle: Boolean = false
    var uploadItem: VideoLocal? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleId = arguments?.getString(Const.Article.ID).toString()
        articleName = arguments?.getString(Const.Article.NAME).toString()
        createArticle = arguments!!.getBoolean(Const.Args.CREATE_ARTICLE)
        owner = arguments?.getParcelable(Const.Args.USER)!!
        return inflater.inflate(R.layout.fragment_attachments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        observeVideos()
        observeLoading()
        observeUploadStatus(this)
        observeDeleteStatus(this, rootActivity)
        setOnClickListeners()
        setVisibilities()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getArticleVideos(arguments?.getString(Const.Article.ID).toString())
    }

    override fun saveQrCodes() {
        val article = ArticleLocal(id = articleId, name = articleName, owner = getUser())
        adapter.list.forEach { video ->
            if (!video.upload) {
                val qrCodeModel =
                    QrCodeModel(
                        url = video.url.toString(),
                        fileType = Const.FileType.VIDEO,
                        name = video.name,
                        article = article,
                        owner = getUser()
                    )
                generateAndSaveQrCode(qrCodeModel, articleName)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setVisibilities() {
        if (createArticle || owner == getUser())
            fab_qr_code.visibility = View.GONE
        else
            fab_qr_code.visibility = View.VISIBLE
    }

    override fun getAttachments() {
        viewModel.getArticleVideos(arguments?.getString(Const.Article.ID).toString())
    }

    private fun setOnClickListeners() {
        fab_qr_code.setOnClickListener {
            RxPermissions(this).request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).subscribe { granted ->
                if (granted)
                    startActivityForResult(
                        QrCodeScanner.newIntent(rootActivity),
                        Const.RequestCode.QR_CODE
                    )
                else
                    rootActivity.toast(getString(R.string.camera_permission))
            }
        }
    }

    private fun observeLoading() {
        viewModel.showLoadingLiveData.observe(this, Observer {
            rootActivity.showLoading(it)
        })
    }

    private fun observeVideos() {
        viewModel.videosLiveData.observe(this, Observer { response ->
            if (response.data != null) {
                if (response.data.isEmpty() && createArticle)
                    return@Observer
                uploadItem?.let {
                    response.data.add(it)
                }
                val minusList = response.data.minus(adapter.list)
                val currentList = adapter.list
                if (currentList.isNotEmpty() && createArticle) {
                    minusList.forEach { videoLocal ->
                        val video = currentList.find { videoLocal.name == it.name }
                        if (video != null) {
                            currentList[currentList.indexOf(video)] = videoLocal
                        }
                    }
                    adapter.submitList(currentList)
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.submitList(response.data)
                }
            }
        })
    }

    private fun initRecycler() {
        val initList = mutableListOf<VideoLocal>()
        if (createArticle || owner == getUser()) {
            uploadItem = VideoLocal(null)
            uploadItem?.let {
                it.upload = true
                it.name = getString(R.string.upload_video)
                initList.add(it)
            }
        }
        adapter =
            VideosAdapter(
                initList, clickListener = {
                    if (it.upload) {
                        startGalleryIntent()
                    } else if (it.isSent) {
                        startActivity(
                            VideoPlayerActivity.newIntentWithUri(
                                rootActivity,
                                it.url!!,
                                false
                            )
                        )
                    }
                }, longClickListener = {
                    val options = listOf(getString(R.string.delete))
                    rootActivity.selector("", options) { dialogInterface, i ->
                        if (options[i] == getString(R.string.delete)) {
                            viewModel.deleteFile(
                                articleId = articleId,
                                name = it.name,
                                fileType = Const.FileType.IMAGE
                            )
                        }
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
        } else if (requestCode == Const.RequestCode.QR_CODE && resultCode == Activity.RESULT_OK) {
            val json = (data!!.extras?.get(Const.Args.KEY_QR_CODE).toString())
            openAttachment(rootActivity, json, false)
        }
    }


    companion object {
        fun newInstance(
            articleId: String,
            createArticle: Boolean,
            articleName: String,
            user: LocalUser
        ): VideosFragment {
            val bundle = Bundle()
            bundle.putString(Const.Article.ID, articleId)
            bundle.putString(Const.Article.NAME, articleName)
            bundle.putBoolean(Const.Args.CREATE_ARTICLE, createArticle)
            bundle.putParcelable(Const.Args.USER, user)
            val fragment =
                VideosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}