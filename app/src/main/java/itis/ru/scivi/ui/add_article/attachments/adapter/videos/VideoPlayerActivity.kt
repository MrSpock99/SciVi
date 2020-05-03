package itis.ru.scivi.ui.add_article.attachments.adapter.videos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import itis.ru.scivi.utils.Const
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : Activity(), Player.EventListener {

    var player: SimpleExoPlayer? = null

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {

    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        finish()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY)
            progressBar.visibility = View.INVISIBLE
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(itis.ru.scivi.R.layout.activity_video_player)
        initializePlayer()
        buildMediaSource(intent.extras?.get(Const.Args.FILE_PATH) as Uri)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onRestart() {
        super.onRestart()
        resumePlayer()
    }

    private fun pausePlayer() {
        if (player != null) {
            player!!.playWhenReady = false
            player!!.playbackState
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    private fun resumePlayer() {
        if (player != null) {
            player!!.playWhenReady = true
            player!!.playbackState
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            val loadControl: LoadControl = DefaultLoadControl(
                DefaultAllocator(true, 16),
                Const.VideoPlayerConfig.MIN_BUFFER_DURATION,
                Const.VideoPlayerConfig.MAX_BUFFER_DURATION,
                Const.VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                Const.VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true
            )
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory: TrackSelection.Factory =
                AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this), trackSelector,
                loadControl
            )
            simpleExoPlayerView.setPlayer(player)
        }
    }

    private fun buildMediaSource(mUri: Uri) {
        val bandwidthMeter = DefaultBandwidthMeter()
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, this.resources.getString(itis.ru.scivi.R.string.app_name)),
            bandwidthMeter
        )
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mUri)
        // Prepare the player with the source.
        player!!.prepare(videoSource)
        player!!.playWhenReady = true
        player!!.addListener(this)
    }

    companion object {
        fun newIntent(context: Context, uri: Uri): Intent {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra(Const.Args.FILE_PATH, uri)
            return intent
        }
    }
}