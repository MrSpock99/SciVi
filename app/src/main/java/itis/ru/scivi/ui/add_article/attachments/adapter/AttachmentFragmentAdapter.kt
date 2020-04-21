package itis.ru.scivi.ui.add_article.attachments.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import itis.ru.scivi.ui.add_article.attachments.adapter.photos.PhotosFragment
import itis.ru.scivi.ui.add_article.attachments.adapter.videos.VideosFragment

class AttachmentFragmentAdapter(fm: FragmentManager, private val articleId: String,
                                private val createArticle: Boolean) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PhotosFragment.newInstance(articleId, createArticle)
            }
            1 -> VideosFragment.newInstance(articleId)
            else -> {
                return PhotosFragment.newInstance(articleId, createArticle)
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