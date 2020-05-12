package itis.ru.scivi.ui.article.attachments.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import itis.ru.scivi.R
import itis.ru.scivi.model.LocalUser
import itis.ru.scivi.ui.article.attachments.adapter.photos.PhotosFragment
import itis.ru.scivi.ui.article.attachments.adapter.videos.VideosFragment

class AttachmentFragmentAdapter(
    fm: FragmentManager, articleId: String,
    createArticle: Boolean,
    articleName: String,
    user: LocalUser,
    private val context: Context
) :
    FragmentStatePagerAdapter(fm) {

    var photosFragment =
        PhotosFragment.newInstance(articleId, createArticle, articleName = articleName, user = user)
    var videosFragment =
        VideosFragment.newInstance(articleId, createArticle, articleName = articleName, user = user)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                photosFragment
            }
            1 -> videosFragment
            else -> {
                return photosFragment
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.photos)
            1 -> context.getString(R.string.videos)
            else -> {
                return context.getString(R.string.ar)
            }
        }
    }

    fun saveQrCodes() {
        photosFragment.saveQrCodes()
        videosFragment.saveQrCodes()
    }
}