package itis.ru.scivi.ui.search

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.tbruyelle.rxpermissions2.RxPermissions
import itis.ru.scivi.R
import itis.ru.scivi.ui.article.QrCodeScanner
import itis.ru.scivi.ui.article.attachments.adapter.AttachmentFragment
import itis.ru.scivi.ui.base.BaseFragment
import itis.ru.scivi.utils.Const
import itis.ru.scivi.utils.dpToPx
import itis.ru.scivi.utils.showKeyboard
import kotlinx.android.synthetic.main.fragment_search.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import org.jetbrains.anko.toast
import org.kodein.di.generic.instance


class SearchFragment : BaseFragment(), AttachmentFragment {
    private val viewModeFactory: ViewModelProvider.Factory by instance()
    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(SearchViewModel::class.java)
    }

    private lateinit var transitionsContainer: ViewGroup
    private lateinit var keyboardVisibilityEvent: Unregistrar
    private lateinit var adapter: SearchArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transitionsContainer = view.findViewById(R.id.search_container) as ViewGroup
        setOnClickListeners()
        initRecycler()
        setTextChangeListener()
        observeSearchResults()
    }

    override fun onStart() {
        super.onStart()
        if(rv_articles.visibility == View.VISIBLE){
            moveUp(false)
            context?.let { showKeyboard(it) }
        }
        keyboardVisibilityEvent = KeyboardVisibilityEvent.registerEventListener(
            activity
        ) { visible ->
            if (visible) {
                moveUp(true)
                rv_articles.visibility = View.VISIBLE
            } else {
                animateDown()
                rv_articles.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.QR_CODE && resultCode == Activity.RESULT_OK) {
            openAttachment(
                rootActivity,
                data!!.extras!!.get(Const.Args.KEY_QR_CODE).toString(),
                true
            )
        }
    }

    override fun onStop() {
        super.onStop()
        keyboardVisibilityEvent.unregister()
    }

    private fun moveUp(animate: Boolean) {
        if(animate)
            TransitionManager.beginDelayedTransition(transitionsContainer, getTransition())
        tv_app_name.visibility = View.GONE
        tv_or.visibility = View.GONE
        iv_qrcode.visibility = View.GONE

        val marginParams = (et_search.layoutParams as ViewGroup.MarginLayoutParams)
        marginParams.marginStart = dpToPx(2)
        marginParams.marginEnd = dpToPx(2)
        et_search.layoutParams = marginParams

        val constraintSet = ConstraintSet()
        constraintSet.clone(search_container)
        constraintSet.connect(
            R.id.et_search, ConstraintSet.TOP, R.id.search_container, ConstraintSet.TOP, dpToPx(8)
        )
        constraintSet.applyTo(search_container)
    }

    private fun animateDown() {
        TransitionManager.beginDelayedTransition(transitionsContainer, getTransition())
        tv_app_name.visibility = View.VISIBLE
        tv_or.visibility = View.VISIBLE
        iv_qrcode.visibility = View.VISIBLE

        val params = (et_search.layoutParams as ViewGroup.MarginLayoutParams)
        params.marginStart = dpToPx(16)
        params.marginEnd = dpToPx(16)
        et_search.layoutParams = params

        val constraintSet = ConstraintSet()
        constraintSet.clone(search_container)
        constraintSet.connect(
            R.id.et_search,
            ConstraintSet.TOP,
            R.id.tv_app_name,
            ConstraintSet.BOTTOM,
            dpToPx(48)
        )
        constraintSet.applyTo(search_container)
    }

    private fun getTransition(): Transition {
        val changeBounds: Transition = ChangeBounds()
        changeBounds.duration = 3000
        changeBounds.interpolator = LinearInterpolator()
        return changeBounds
    }

    private fun setOnClickListeners() {
        fab_add.setOnClickListener {
            rootActivity.navController.navigate(R.id.action_searchFragment_to_addArticleFragment)
            rv_articles.visibility = View.GONE
        }
        iv_qrcode.setOnClickListener {
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

    private fun setTextChangeListener() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    pb_downloading.visibility = View.VISIBLE
                    rv_articles.visibility = View.VISIBLE
                    viewModel.getArticlesByKeyword(s.toString())
                } else {
                    rv_articles.visibility = View.GONE
                    pb_downloading.visibility = View.GONE
                }
            }
        })
    }

    private fun observeSearchResults() {
        viewModel.articlesLiveData.observe(this, Observer { response ->
            pb_downloading.visibility = View.GONE
            if (response.data != null) {
                adapter.submitList(response.data)
            }
        })
    }

    private fun initRecycler() {
        adapter = SearchArticlesAdapter(arrayListOf()) {
            val action =
                SearchFragmentDirections.actionSearchFragmentToAddAttachmentsFragment(
                    it, createArticle = false, user = it.owner
                )
            rootActivity.navController.navigate(action)
        }
        rv_articles.adapter = adapter
        rv_articles.layoutManager = LinearLayoutManager(context)
    }

    override fun saveQrCodes() {
    }

    override fun setVisibilities() {
    }
}