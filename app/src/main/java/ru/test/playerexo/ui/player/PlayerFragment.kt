package ru.test.playerexo.ui.player

import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import ru.test.playerexo.MainActivity
import ru.test.playerexo.R
import ru.test.playerexo.databinding.FragmentPlayerBinding
import ru.test.playerexo.model.ui.ChannelUI

class PlayerFragment : Fragment(), Player.Listener {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var player: ExoPlayer
    private lateinit var settingsBtn: ImageView
    private lateinit var bitRates: LinearLayoutCompat
    private lateinit var trackSelector: DefaultTrackSelector
    private var activeTrack = 0
    var qualityList = ArrayList<Pair<String, TrackSelectionOverrides.Builder>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).apply {
            statusBarVisibility(false)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val channel: ChannelUI? = arguments?.getParcelable(KEY)
        trackSelector = DefaultTrackSelector(requireContext(), AdaptiveTrackSelection.Factory())
        player = ExoPlayer.Builder(requireContext()).setTrackSelector(trackSelector).build()
        binding.playerView.player = player
        if (channel != null) {
            val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            val mediaItem = MediaItem.fromUri(
                "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
//                    "https://devstreaming-cdn.apple.com/videos/" +
//                            "streaming/examples/img_bipbop_adv_example_fmp4" +
//                            "/master.m3u8"
            )
            val mediaSource =
                HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
            player.setMediaSource(mediaSource)
            player.addListener(this)
            player.prepare()
        }
        bitRates = binding.root.findViewById(R.id.bitRates)
        binding.playerView.setControllerVisibilityListener {
            if (it == View.GONE) bitRates.isVisible = false
        }
        settingsBtn = view.findViewById(com.google.android.exoplayer2.ui.R.id.exo_settings)
        settingsBtn.setOnClickListener { bitRates.isVisible = !bitRates.isVisible }
    }

    fun DefaultTrackSelector.generateQualityList(): ArrayList<Pair<String, TrackSelectionOverrides.Builder>> {
        val trackOverrideList = ArrayList<Pair<String, TrackSelectionOverrides.Builder>>()
        val renderTrack = this.currentMappedTrackInfo
        val renderCount = renderTrack?.rendererCount ?: 0
        for (rendererIndex in 0 until renderCount) {
            if (isSupportedFormat(renderTrack, rendererIndex)) {
                val trackGroupType = renderTrack?.getRendererType(rendererIndex)
                val trackGroups = renderTrack?.getTrackGroups(rendererIndex)
                val trackGroupsCount = trackGroups?.length!!
                if (trackGroupType == C.TRACK_TYPE_VIDEO) {
                    for (groupIndex in 0 until trackGroupsCount) {
                        val videoQualityTrackCount = trackGroups[groupIndex].length
                        for (trackIndex in 0 until videoQualityTrackCount) {
                            val isTrackSupported = renderTrack.getTrackSupport(
                                rendererIndex, groupIndex, trackIndex
                            ) == C.FORMAT_HANDLED
                            if (isTrackSupported) {
                                val track = trackGroups[groupIndex]
                                val trackName = "${track.getFormat(trackIndex).height}p"
                                val trackBuilder = TrackSelectionOverrides.Builder()
                                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO).addOverride(
                                        TrackSelectionOverrides.TrackSelectionOverride(
                                            track, listOf(trackIndex)
                                        )
                                    )
                                trackOverrideList.add(Pair(trackName, trackBuilder))
                            }
                        }
                    }
                }
            }
        }
        return trackOverrideList
    }

    private fun setUpQualityList() {
        bitRates.removeAllViews()
        qualityList.let {
            for ((index, videoQuality) in it.withIndex().reversed()) {
                bitRates.addView(BitrateItem(requireContext()).apply {
                    setBackground(
                        index == activeTrack,
                        index == 0,
                        index + 1 == qualityList.size
                    )
                    text = videoQuality.first
                    gravity = Gravity.CENTER
                    minHeight = 40f.toPx.toInt()
                    minWidth = 128f.toPx.toInt()
                    setOnClickListener {
                        activeTrack = index
                        qualityList[index].let { pair ->
                            trackSelector.parameters = trackSelector.parameters.buildUpon()
                                .setTrackSelectionOverrides(pair.second.build())
                                .setTunnelingEnabled(true).build()
                        }
                    }
                })
            }
        }
    }

    private val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        )

    override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
        println("TRACK CHANGED")
        println(tracksInfo.trackGroupInfos)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY) {
            trackSelector.generateQualityList().let {
                qualityList = it
                qualityList.add(
                    0, Pair(
                        "Auto",
                        TrackSelectionOverrides.Builder().clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                    )
                )
                setUpQualityList()
            }
        }
    }

    private fun releasePlayer() {
        player.release()
    }

    fun isSupportedFormat(
        mappedTrackInfo: MappingTrackSelector.MappedTrackInfo?, rendererIndex: Int
    ): Boolean {
        val trackGroupArray = mappedTrackInfo?.getTrackGroups(rendererIndex)
        return if (trackGroupArray?.length == 0) false
        else mappedTrackInfo?.getRendererType(rendererIndex) == C.TRACK_TYPE_VIDEO
    }

    override fun onResume() {
        super.onResume()
        player.play()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        _binding = null
        releasePlayer()
        super.onDestroy()
    }

    companion object {
        private const val KEY = "KEY"
        fun newInstance(channel: ChannelUI): PlayerFragment {
            val b = Bundle().also { it.putParcelable(KEY, channel) }
            return PlayerFragment().also { it.arguments = b }
        }
    }
}
