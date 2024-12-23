package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private val binding: ActivityRootBinding by lazy { ActivityRootBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = (supportFragmentManager.findFragmentById(R.id.main_fragment_container_view)
            as NavHostFragment).navController
        binding.mainNavigationView.setupWithNavController(
            navController
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.vacancy_details_fragment, R.id.work_location_fragment, R.id.industries_fragment -> {
                    binding.mainNavigationView.isVisible = false
                    binding.mainNavigationViewSplitter.isVisible = false
                }

                else -> {
                    binding.mainNavigationView.isVisible = true
                    binding.mainNavigationViewSplitter.isVisible = true
                }
            }
        }
    }
}
