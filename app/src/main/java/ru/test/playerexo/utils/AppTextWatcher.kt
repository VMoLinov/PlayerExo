package ru.test.playerexo.utils

import android.text.Editable
import android.text.TextWatcher

class AppTextWatcher(private val onChange: (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        onChange(s.toString())
    }
}
