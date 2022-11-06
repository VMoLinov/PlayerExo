package ru.test.playerexo.ui.player

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import ru.test.playerexo.R

class BitrateItem(context: Context) : androidx.appcompat.widget.AppCompatTextView(context) {

    fun setBackground(isActive: Boolean, isFirst: Boolean, isLast: Boolean) {
        when {
            isActive -> {
                if (isFirst) background(R.drawable.bitrates_background_active_first)
                else if (isLast) background(R.drawable.bitrates_background_active_last)
                else background(R.color.bitrate_active)
            }
            else -> {
                if (isFirst) background(R.drawable.bitrates_background_first)
                else if (isLast) background(R.drawable.bitrates_background_last)
                else background(R.color.white)
            }
        }
    }

    private fun background(res: Int) {
        background = ResourcesCompat.getDrawable(resources, res, null)
    }
}
