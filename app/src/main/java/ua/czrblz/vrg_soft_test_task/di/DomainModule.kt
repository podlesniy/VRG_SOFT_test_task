package ua.czrblz.vrg_soft_test_task.di

import org.koin.dsl.module
import org.koin.dsl.single
import ua.czrblz.domain.usecase.DownloadPictureInGalleryUseCase
import ua.czrblz.domain.usecase.GetTopPostsUseCase
import ua.czrblz.domain.usecase.OpenPictureInLargeFormatUseCase

val domainModule = module {

    single {
        GetTopPostsUseCase(
            get()
        )
    }

    single {
        OpenPictureInLargeFormatUseCase(
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