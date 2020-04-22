package itis.ru.scivi.ui.add_article.attachments.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import itis.ru.scivi.ui.add_article.attachments.adapter.photos.PhotosFragment
import itis.ru.scivi.ui.add_article.attachments.adapter.videos.VideosFragment

class AttachmentFragmentAdapter(
    fm: FragmentManager, articleId: String,
    createArticle: Boolean
) :
    FragmentStatePagerAdapter(fm) {

    private var photosFragment = PhotosFragment.newInstance(articleId, createArticle)
    private var videosFragment = VideosFragment.newInstance(articleId)

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
            0 -> "Photos"
            1 -> "Videos"
            else -> {
                return "AR"
            }
        }
    }
}