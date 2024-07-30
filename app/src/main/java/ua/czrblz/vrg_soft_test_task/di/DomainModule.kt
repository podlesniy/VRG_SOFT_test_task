package ua.czrblz.vrg_soft_test_task.di

import org.koin.dsl.module
import ua.czrblz.domain.usecase.DownloadPictureInGalleryUseCase
import ua.czrblz.domain.usecase.GetTopPostsUseCase

val domainModule = module {

    single {
        GetTopPostsUseCase(
            get()
        )
    }

    single {
        DownloadPictureInGalleryUseCase(
            get(),
            get()
        )
    }
}