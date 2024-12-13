package com.libbib.spewnikpl.presentation.OptionsFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libbib.spewnikpl.domain.options.GetOptionsUseCase
import com.libbib.spewnikpl.domain.options.Options
import com.libbib.spewnikpl.domain.options.SaveOptionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OptionsViewModel @Inject constructor(
    private val getOptionsUseCase: GetOptionsUseCase,
    private val saveOptionsUseCase: SaveOptionsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<OptionsFragmentState>(OptionsFragmentState.Progress)
    val state = _state.asStateFlow()

    private lateinit var actualState: Options

    init {
        viewModelScope.launch {
            actualState = getOptionsUseCase()
            updateState()
        }
    }

    private fun updateState() {
        _state.value = OptionsFragmentState.Content(actualState)
    }

    suspend fun saveOptions() {
        _state.value = OptionsFragmentState.Progress
        val saveOptionsJob = viewModelScope.launch {
            saveOptionsUseCase(actualState)
            _state.value = OptionsFragmentState.Content(actualState)
        }
        saveOptionsJob.join()
    }

    fun onTransposePlusListener() {
        if (actualState.transposeInt + 1 <= MAX_TRANSPOSE_NUMBER) {
            actualState.transposeInt += 1
            updateState()
        }
    }

    fun onTransposeMinusListener() {
        if (actualState.transposeInt - 1 >= MIN_TRANSPOSE_NUMBER) {
            actualState.transposeInt -= 1
            updateState()
        }
    }

    fun onTextSizePlusListener() {
        actualState.textSize += 1
        updateState()
    }

    fun onTextSizeMinusListener() {
        actualState.textSize -= 1
        updateState()
    }

    fun onShowChordsListener() {
        actualState.isChordsVisible = !actualState.isChordsVisible
        updateState()
    }

    fun onDarkModeListener() {
        actualState.isDarkMode = !actualState.isDarkMode
        updateState()
    }

    fun setColor(color: Int) {
        actualState.chordsColor = color
        updateState()
    }
    companion object{
        private const val MAX_TRANSPOSE_NUMBER = 12
        private const val MIN_TRANSPOSE_NUMBER = -12
    }

}