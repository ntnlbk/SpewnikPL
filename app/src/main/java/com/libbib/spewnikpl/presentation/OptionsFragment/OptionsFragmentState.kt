package com.libbib.spewnikpl.presentation.OptionsFragment

import com.libbib.spewnikpl.domain.options.Options


sealed class OptionsFragmentState {
    data object Progress : OptionsFragmentState()
    class Content(
        val options: Options,
    ) : OptionsFragmentState()
}
