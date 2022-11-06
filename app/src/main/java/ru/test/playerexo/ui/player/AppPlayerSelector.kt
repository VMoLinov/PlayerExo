package ru.test.playerexo.ui.player

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides

class AppPlayerSelector(
    context: Context, factory: AdaptiveTrackSelection.Factory
) : DefaultTrackSelector(context, factory) {

    private var renderCount = 0
    private var renderTrack: MappedTrackInfo? = null
    private var trackOverrideList =
        ArrayList<Pair<String, TrackSelectionOverrides.Builder>>()

    internal fun generateQualityList():
            ArrayList<Pair<String, TrackSelectionOverrides.Builder>> {
        trackOverrideList = arrayListOf()
        renderTrack = this.currentMappedTrackInfo
        renderCount = renderTrack?.rendererCount ?: 0
        for (rendererIndex in 0 until renderCount) {
            checkType(rendererIndex)
        }
        return trackOverrideList
    }

    private fun checkType(rendererIndex: Int) {
        if (isSupportedFormat(rendererIndex)) {
            val trackGroupType = renderTrack?.getRendererType(rendererIndex)
            val trackGroups = renderTrack?.getTrackGroups(rendererIndex)
            val trackGroupsCount = trackGroups?.length!!
            if (trackGroupType == C.TRACK_TYPE_VIDEO) {
                checkSupported(trackGroupsCount, trackGroups, rendererIndex)
            }
        }
    }

    private fun checkSupported(
        trackGroupsCount: Int, trackGroups: TrackGroupArray, rendererIndex: Int
    ) {
        for (groupIndex in 0 until trackGroupsCount) {
            val videoQualityTrackCount = trackGroups[groupIndex].length
            for (trackIndex in 0 until videoQualityTrackCount) {
                val isTrackSupported = renderTrack?.getTrackSupport(
                    rendererIndex, groupIndex, trackIndex
                ) == C.FORMAT_HANDLED
                if (isTrackSupported) {
                    addTracksToList(trackGroups, groupIndex, trackIndex)
                }
            }
        }
    }

    private fun addTracksToList(
        trackGroups: TrackGroupArray, groupIndex: Int, trackIndex: Int
    ) {
        val track = trackGroups[groupIndex]
        val trackName = "${track.getFormat(trackIndex).height}$QUALITY_SUFFIX"
        val trackBuilder = TrackSelectionOverrides.Builder()
            .clearOverridesOfType(C.TRACK_TYPE_VIDEO).addOverride(
                TrackSelectionOverrides
                    .TrackSelectionOverride(track, listOf(trackIndex))
            )
        trackOverrideList.add(Pair(trackName, trackBuilder))
    }

    private fun isSupportedFormat(rendererIndex: Int): Boolean {
        val trackGroupArray = renderTrack?.getTrackGroups(rendererIndex)
        return if (trackGroupArray?.length == 0) false
        else renderTrack?.getRendererType(rendererIndex) == C.TRACK_TYPE_VIDEO
    }

    companion object {
        const val QUALITY_SUFFIX = "p"
    }
}
