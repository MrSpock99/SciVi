package itis.ru.scivi.ui.article.attachments.adapter.photos

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
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import com.tbruyelle.rxpermissions2.RxPermissions
import itis.ru.scivi.R
import itis.ru.scivi.model.ArticleLocal
import itis.ru.scivi.model.PhotoLocal
import itis.ru.scivi.model.QrCodeModel
import itis.ru.scivi.ui.article.QrCodeScanner
import itis.ru.scivi.ui.article.attachments.AttachmentNameActivity
import itis.ru.scivi.ui.article.attachments.adapter.AttachmentFragment
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.dpToPx
import itis.ru.scivi.workers.UploadWorker
import kotlinx.android.synthetic.main.fragment_attachments.*
import org.jetbrains.anko.toast
import org.kodein.di.generic.instance


class PhotosFragment : BaseFragment(), AttachmentFragment {
    private lateinit var adapter: PhotosAdapter
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: PhotosViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(PhotosViewModel::class.java)
    }
    private lateinit var articleId: String
    private lateinit var articleName: String
    private var createArticle: Boolean = false
    var uploadItem: PhotoLocal? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleId = arguments?.getString(Const.Article.ID).toString()
        articleName = arguments?.getString(Const.Article.NAME).toString()
        createArticle = arguments!!.getBoolean(Const.Args.CREATE_ARTICLE)
        return inflater.inflate(R.layout.fragment_attachments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        observePhotos()
        observeLoading()
        observeUploadStatus()
        setOnClickListeners()
        setVisibilities()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getArticlePhotos(arguments?.getString(Const.Article.ID).toString())
    }

    override fun saveQrCodes() {
        val article = ArticleLocal(id = articleId, name = articleName)
        adapter.list.forEach { photo ->
            if (!photo.upload) {
                val qrCodeModel =
                    QrCodeModel(
                        url = photo.url.toString(),
                        fileType = Const.FileType.IMAGE,
                        name = photo.name,
                        article = article
                    )
                generateAndSaveQrCode(qrCodeModel, articleName)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setVisibilities() {
        if (createArticle)
            fab_qr_code.visibility = View.GONE
        else
            fab_qr_code.visibility = View.VISIBLE
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

    private fun observeUploadStatus() {
        WorkManager.getInstance().getWorkInfosByTagLiveData(UploadWorker::class.toString())
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    if (workInfo.any { it.state == WorkInfo.State.SUCCEEDED })
                        viewModel.getArticlePhotos(arguments?.getString(Const.Article.ID).toString())
                }
            })
    }

    private fun observeLoading() {
        viewModel.showLoadingLiveData.observe(this, Observer {
            rootActivity.showLoading(it)
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
                val minusList = response.data.minus(adapter.list)
                val currentList = adapter.list
                if (currentList.isNotEmpty()) {
                    minusList.forEach { photoLocal ->
                        currentList[currentList.indexOf(currentList.find { photoLocal.name == it.name })] =
                            photoLocal
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
        val initList = mutableListOf<PhotoLocal>()
        if (createArticle) {
            uploadItem = PhotoLocal(null)
            uploadItem?.let {
                it.upload = true
                it.name = getString(R.string.upload_photo)
                initList.add(it)
            }
        }
        adapter =
            PhotosAdapter(
                initList
            ) {
                if (it.upload) {
                    startGalleryIntent()
                } else {
                    StfalconImageViewer.Builder<PhotoLocal>(
                        context,
                        arrayListOf(it)
                    ) { view, image ->
                        Glide.with(context!!).load(image.url).into(view)
                    }.show()
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
                startActivityForResult(
                    AttachmentNameActivity.newIntent(rootActivity, uri, Const.FileType.IMAGE),
                    Const.RequestCode.ATTACHMENT_NAME
                )
            }
        } else if (requestCode == Const.RequestCode.ATTACHMENT_NAME && resultCode == Activity.RESULT_OK) {
            val photoLocal = data!!.extras?.get(Const.Args.ATTACHMENT) as PhotoLocal
            photoLocal.isSent = false
            adapter.list.add(0, photoLocal)
            adapter.notifyItemInserted(0)
            adapter.submitList(adapter.list)
            viewModel.uploadFile(
                photoLocal.url!!,
                articleId,
                Const.FileType.IMAGE,
                photoLocal.name
            )
        } else if (requestCode == Const.RequestCode.QR_CODE && resultCode == Activity.RESULT_OK) {
            openAttachment(
                rootActivity,
                data!!.extras!!.get(Const.Args.KEY_QR_CODE).toString(),
                false
            )
        }
    }

    companion object {
        fun newInstance(
            articleId: String,
            createArticle: Boolean,
            articleName: String
        ): PhotosFragment {
            val bundle = Bundle()
            bundle.putString(Const.Article.ID, articleId)
            bundle.putString(Const.Article.NAME, articleName)
            bundle.putBoolean(Const.Args.CREATE_ARTICLE, createArticle)
            val fragment =
                PhotosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}