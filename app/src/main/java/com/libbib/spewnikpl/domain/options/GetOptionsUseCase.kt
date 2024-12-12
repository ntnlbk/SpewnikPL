package com.libbib.spewnikpl.domain.options

import javax.inject.Inject

class GetOptionsUseCase @Inject constructor(private val optionsRepository: OptionsRepository) {
    suspend operator fun invoke() = optionsRepository.getOptions()
}