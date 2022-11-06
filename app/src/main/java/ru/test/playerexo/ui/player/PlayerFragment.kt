package ru.test.playerexo.ui.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import ru.test.playerexo.MainActivity
import ru.test.playerexo.R
import ru.test.playerexo.databinding.FragmentPlayerBinding
import ru.test.playerexo.model.ui.ChannelUI

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var player: ExoPlayer

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
        val def = DefaultTrackSelector(requireContext())
        def.setParameters(
            def
                .buildUponParameters()
                .setAllowVideoMixedMimeTypeAdaptiveness(true)
        );
        player = ExoPlayer.Builder(requireContext()).setTrackSelector(def).build()
        binding.playerView.player = player
        if (channel != null) {
            val media = MediaItem.Builder()
                .setUri("https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8")
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()
            val selector = player.trackSelectionParameters
            player.setMediaItem(media)
            player.prepare()
        }
        val btn = view.findViewById<ImageView>(com.google.android.exoplayer2.ui.R.id.exo_settings)
        btn.setOnClickListener {
            val menu = PopupMenu(context, it)
            menu.menuInflater.inflate(R.menu.settings_menu, menu.menu)
            menu.setOnMenuItemClickListener {
                val traks = player.currentTracks
                traks.groups.forEach {
                    Log.d("TAG", "onViewCreated: ${it.getTrackFormat(C.TRACK_TYPE_VIDEO)}")
                }
                true
            }
            menu.show()
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
        private const val KEY = "KEY"
        fun newInstance(channel: ChannelUI): PlayerFragment {
            val b = Bundle().also { it.putParcelable(KEY, channel) }
            return PlayerFragment().also { it.arguments = b }
        }
    }
}
