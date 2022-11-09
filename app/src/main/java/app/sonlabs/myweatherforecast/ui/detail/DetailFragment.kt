package app.sonlabs.myweatherforecast.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.sonlabs.myweatherforecast.data.UiResponse
import app.sonlabs.myweatherforecast.databinding.FragmentDetailBinding
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by inject()
    private var isMetric: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.viewModel = viewModel
        arguments?.apply {
            val data = DetailFragmentArgs.fromBundle(this)
            isMetric = data.units
            viewModel.getDetail(data.lat.toDouble(), data.lon.toDouble(), data.units)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewModel.detail.collect { response ->
                when (response) {
                    is UiResponse.Error -> {
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    UiResponse.Idle -> Unit
                    UiResponse.Loading -> Unit
                    is UiResponse.Success -> Unit
                }
            }
        }
    }
}
