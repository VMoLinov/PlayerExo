package ru.test.playerexo.ui.player

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import ru.test.playerexo.R

class BitrateItem(context: Context) : AppCompatTextView(context) {

    private fun setBackground(isActive: Boolean, isFirst: Boolean, isLast: Boolean) {
        if (isActive) {
            setTextColor(resources.getColor(R.color.white, null))
            if (isFirst) background(R.drawable.bitrates_background_active_first)
            else if (isLast) background(R.drawable.bitrates_background_active_last)
            else background(R.color.bitrate_active)
        } else {
            if (isFirst) background(R.drawable.bitrates_background_first)
            else if (isLast) background(R.drawable.bitrates_background_last)
            else background(R.color.white)
        }
    }

    private fun background(res: Int) {
        background = ResourcesCompat.getDrawable(resources, res, null)
    }

    fun buildView(
        index: Int,
        activeTrack: Int,
        qualityList: Int,
        qualityString: String
    ) {
        setBackground(index == activeTrack, index == 0, index + 1 == qualityList)
        text = qualityString
        gravity = Gravity.CENTER
//        textSize = 16f.spToPx
        minHeight = HEIGHT.dpToPx
        minWidth = WIDTH.dpToPx
    }

    private val Number.dpToPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()

//    private val Number.spToPx
//        get() = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics
//        )

    companion object {
        const val HEIGHT = 40f
        const val WIDTH = 128f
    }
}
