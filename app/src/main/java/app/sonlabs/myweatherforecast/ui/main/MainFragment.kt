package app.sonlabs.myweatherforecast.ui.main

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.sonlabs.myweatherforecast.data.Coord
import app.sonlabs.myweatherforecast.data.UiResponse
import app.sonlabs.myweatherforecast.databinding.FragmentMainBinding
import app.sonlabs.myweatherforecast.util.Constants.UNITS_IMPERIAL
import app.sonlabs.myweatherforecast.util.Constants.UNITS_METRIC
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val mainViewModel: MainViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()

        binding.viewModel = mainViewModel

        mainViewModel.getLocation()

        binding.units.setOnClickListener {
            mainViewModel.setUnits()
            mainViewModel.search(binding.cityName.text.toString())
        }

        binding.buttonSearch.setOnClickListener {
            performSearch()
        }

        binding.cityName.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
        binding.buttonPermission.setOnClickListener {
            requestPermission()
        }

        binding.cardLayout.setOnClickListener {
            mainViewModel.getData()?.let {
                navigateToDetail(it.coord)
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.forecast.collect { response ->
                when (response) {
                    is UiResponse.Error -> {
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    UiResponse.Idle -> {
                        binding.cardLayout.visibility = View.INVISIBLE
                    }
                    is UiResponse.Success -> {
                        binding.cardLayout.visibility = View.VISIBLE

                    }
                    UiResponse.Loading -> {

                    }
                }
            }
        }
    }

    private fun performSearch() {
        if (binding.cityName.text.toString().trim().isNotEmpty()) {
            val inputManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.cityName.windowToken, 0)
            mainViewModel.search(binding.cityName.text.toString().trim())
        }
    }

    private fun navigateToDetail(it: Coord) {
        findNavController().navigate(
            MainFragmentDirections.actionMainToDetail(
                it.lat.toString(),
                it.lon.toString(),
                mainViewModel.units.value
            )
        )
    }

    private fun requestPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            binding.buttonPermission.visibility = View.GONE
                        } else {
                            binding.buttonPermission.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }
}