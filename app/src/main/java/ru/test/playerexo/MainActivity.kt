package ru.test.playerexo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ru.test.playerexo.databinding.ActivityMainBinding
import ru.test.playerexo.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var windowInsetsController: WindowInsetsControllerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        windowInsetsController = WindowCompat.getInsetsController(window, binding.root)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.systemBarsBehavior
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment.newInstance()).commitNow()
        }
    }

    fun statusBarVisibility(isVisible: Boolean) {
        if (isVisible) windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        else windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}
