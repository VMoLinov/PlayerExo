package ru.test.playerexo.ui.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
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
    private lateinit var trackSelector: AppPlayerSelector
    private var qualityList = ArrayList<Pair<String, TrackSelectionOverrides.Builder>>()
    private var activeTrack = 0

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

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val channelUI = arguments?.getParcelable<ChannelUI>(KEY)
        if (channelUI != null) {
            playerInit(channelUI.url)
            buttonsInit(view)
            fieldsInit(view, channelUI)
        }
    }

    private fun playerInit(url: String) {
        trackSelector = AppPlayerSelector(requireContext(), AdaptiveTrackSelection.Factory())
        val mediaSource = HlsMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(TEST_LINK_2))
//            .createMediaSource(MediaItem.fromUri(url)) // Replace this instead of previous, when source will be work
        player = ExoPlayer.Builder(requireContext()).setTrackSelector(trackSelector)
            .build().apply {
                binding.playerView.player = this
                setMediaSource(mediaSource)
                addListener(this@PlayerFragment)
                prepare()
            }
    }

    private fun buttonsInit(view: View) {
        bitRates = view.findViewById(R.id.bitRates)
        settingsBtn = view.findViewById(com.google.android.exoplayer2.ui.R.id.exo_settings)
        settingsBtn.setOnClickListener { bitRates.isVisible = !bitRates.isVisible }
        view.findViewById<ImageView>(R.id.backBtn).apply {
            setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun fieldsInit(view: View, channelUI: ChannelUI) {
        view.apply {
            findViewById<ImageView>(R.id.channelImage).apply {
                Glide.with(view).load(channelUI.image).into(this)
            }
            findViewById<TextView>(R.id.headerPlayer).text = channelUI.current.title
            findViewById<TextView>(R.id.channelNamePlayer).text = channelUI.nameRu
            setTimeRemaining(channelUI.current.timeStop, view)
            binding.playerView.setControllerVisibilityListener {
                if (it == View.GONE) bitRates.isVisible = false
                setTimeRemaining(channelUI.current.timeStop, view)
            }
        }
    }

    private fun setTimeRemaining(timeStop: Long, view: View) {
        val remaining = System.currentTimeMillis() - timeStop
        val timeLeft = remaining / 60000
        view.findViewById<TextView>(R.id.timeLeft).text =
            String.format(getString(R.string.time_remaining), timeLeft)
    }

    private fun setUpQualityList() {
        bitRates.removeAllViews()
        qualityList.let {
            for ((index, videoQuality) in it.withIndex().reversed()) {
                bitRates.addView(BitrateItem(requireContext()).apply {
                    buildView(index, activeTrack, qualityList.size, videoQuality.first)
                    clickListener(index)
                })
            }
        }
    }

    private fun BitrateItem.clickListener(index: Int) {
        setOnClickListener {
            activeTrack = index
            qualityList[index].let { pair ->
                trackSelector.parameters = trackSelector.parameters.buildUpon()
                    .setTrackSelectionOverrides(pair.second.build())
                    .setTunnelingEnabled(true).build()
            }
        }
    }

    override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
        Log.d(FRAGMENT_NAME, "onTracksInfoChanged: TRACK CHANGED")
        Log.d(FRAGMENT_NAME, "onTracksInfoChanged: ${tracksInfo.trackGroupInfos}")
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY) {
            val cleanSelector = TrackSelectionOverrides.Builder()
                .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
            trackSelector.generateQualityList().let {
                qualityList = it
                qualityList.add(0, Pair("AUTO", cleanSelector))
                setUpQualityList()
            }
        }
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
        player.release()
        super.onDestroy()
    }

    companion object {
        //        const val TEST_LINK_1 = "https://devstreaming-cdn.apple.com/videos/" +
//                "streaming/examples/img_bipbop_adv_example_fmp4" +
//                "/master.m3u8"
        const val TEST_LINK_2 = "https://bitdash-a.akamaihd.net/content/" +
                "MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
        val FRAGMENT_NAME: String = PlayerFragment::class.java.simpleName
        private const val KEY = "keyPlayerFragment"
        fun newInstance(channel: ChannelUI): PlayerFragment {
            val b = Bundle().also { it.putParcelable(KEY, channel) }
            return PlayerFragment().also { it.arguments = b }
        }
    }
}
