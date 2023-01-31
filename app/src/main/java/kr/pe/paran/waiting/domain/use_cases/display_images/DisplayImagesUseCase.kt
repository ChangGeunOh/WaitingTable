package kr.pe.paran.waiting.domain.use_cases.display_images

import kr.pe.paran.waiting.data.repository.Repository

class DisplayImagesUseCase(private val repository: Repository) {

    suspend operator fun invoke(): List<String> {
        return repository.loadDisplayImages()
    }

    suspend operator fun invoke(imageList: List<String>) {
        repository.saveDisplayImages(imageList)
    }
}