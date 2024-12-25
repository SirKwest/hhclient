package ru.practicum.android.diploma.ui.regions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.LocationInteractor
import ru.practicum.android.diploma.domain.models.RegionsResource

class RegionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_regions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationInteractor: LocationInteractor by inject()
        lifecycleScope.launch {
            locationInteractor.getRegions("113").collect {
                if (it is RegionsResource.Success) {
                    requireActivity().findViewById<TextView>(R.id.tv).text = it.items.toString()
                }
            }
        }
    }
}
