package itis.ru.scivi.ui.add_article.attachments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import itis.ru.scivi.R
import itis.ru.scivi.model.PhotoLocal
import itis.ru.scivi.model.VideoLocal
import itis.ru.scivi.utils.Const
import kotlinx.android.synthetic.main.activity_attachment_name.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class AttachmentNameActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attachment_name)
        Glide.with(this).load(intent.extras?.get(Const.Args.FILE_PATH)).into(iv_attachment_photo)
        setClickListeners()
    }

    private fun setClickListeners() {
        btn_ok.setOnClickListener {
            if (et_attachment_name.text.isNotEmpty()) {
                val result = Intent()
                if (intent.extras?.getString(Const.Args.FILE_TYPE) == Const.FileType.IMAGE) {
                    val photoLocal = PhotoLocal(intent.extras?.get(Const.Args.FILE_PATH) as Uri)
                    photoLocal.name = et_attachment_name.text.toString()
                    result.putExtra(Const.Args.ATTACHMENT, photoLocal)
                } else if (intent.extras?.getString(Const.Args.FILE_TYPE) == Const.FileType.VIDEO) {
                    val videoLocal = VideoLocal(intent.extras?.get(Const.Args.FILE_PATH) as Uri)
                    videoLocal.name = et_attachment_name.text.toString()
                    result.putExtra(Const.Args.ATTACHMENT, videoLocal)
                }
                setResult(RESULT_OK, result)
                finish()
            } else {
                alert(getString(R.string.emtpy_name), getString(R.string.error)) {
                    yesButton {}
                }.show()
            }
        }
    }

    companion object {
        fun newIntent(context: Context, uri: Uri, fileType: String): Intent {
            val intent = Intent(context, AttachmentNameActivity::class.java)
            intent.putExtra(Const.Args.FILE_PATH, uri)
            intent.putExtra(Const.Args.FILE_TYPE, fileType)
            return intent
        }
    }
}