package com.libbib.spewnikpl.presentation.OptionsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.libbib.spewnikpl.R
import com.libbib.spewnikpl.databinding.FragmentOptionsBinding
import com.libbib.spewnikpl.di.SpewnikApplication
import com.libbib.spewnikpl.di.ViewModelFactory
import com.libbib.spewnikpl.domain.options.Options
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val colorPickerStateName = "MyColorPickerDialog"

class OptionsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as SpewnikApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OptionsViewModel::class.java]
    }

    private var _binding: FragmentOptionsBinding? = null
    private val binding: FragmentOptionsBinding
        get() = _binding ?: throw Exception("FragmentOptionsBinding is null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
        observeViewModel()


    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is OptionsFragmentState.Progress -> {
                        binding.optionsProgressBar.visibility = View.VISIBLE
                    }

                    is OptionsFragmentState.Content -> {
                        updateViewsWithContent(it.options)
                        binding.optionsProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun updateViewsWithContent(it: Options) {
        binding.chordsCb.isChecked = it.isChordsVisible
        binding.transposeNumberTv.text = it.transposeInt.toString()
        binding.textSizeNumberTv.text = it.textSize.toString()
        binding.darkModeCb.isChecked = it.isDarkMode
    }

    private fun setupOnClickListeners() {

        binding.backBtnOptionsFragment.setOnClickListener {
            backPressed()
        }
        binding.showChordsIv.setOnClickListener {
            viewModel.onShowChordsListener()
        }
        binding.chordsCb.setOnClickListener {
            viewModel.onShowChordsListener()
        }
        binding.transposePlusIb.setOnClickListener {
            viewModel.onTransposePlusListener()
        }
        binding.transposeMinusIb.setOnClickListener {
            viewModel.onTransposeMinusListener()
        }

        binding.changeColorIv.setOnClickListener {
            ColorPickerDialog.Builder(requireActivity())
                .setPreferenceName(colorPickerStateName)
                .setPositiveButton(getString(R.string.confirm),
                    ColorEnvelopeListener { envelope, _ -> viewModel.setColor(envelope.color) })
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialogInterface, _ -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show()
        }
        binding.textSizePlusIb.setOnClickListener {
            viewModel.onTextSizePlusListener()
        }
        binding.textSizeMinusIb.setOnClickListener {
            viewModel.onTextSizeMinusListener()
        }
        binding.darkModeIv.setOnClickListener {
            viewModel.onDarkModeListener()
        }
        binding.darkModeCb.setOnClickListener {
            viewModel.onDarkModeListener()
        }
        binding.accept.setOnClickListener {
            lifecycleScope.launch {
                viewModel.saveOptions()
                backPressed()
            }
        }
    }


    private fun backPressed() {
        (requireActivity().application as SpewnikApplication).darkMode()
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {

        fun newInstance() =
            OptionsFragment()

    }
}