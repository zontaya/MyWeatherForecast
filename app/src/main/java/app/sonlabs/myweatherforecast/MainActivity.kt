package app.sonlabs.myweatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import app.sonlabs.myweatherforecast.databinding.ActivityMainOneBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main)
                as NavHostFragment
        val navController = navHostFragment.navController
        AppBarConfiguration(navController.graph)
    }
}
