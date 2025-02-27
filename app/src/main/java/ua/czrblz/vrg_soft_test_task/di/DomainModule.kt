package ua.czrblz.vrg_soft_test_task.di

import org.koin.dsl.module
import ua.czrblz.domain.usecase.SavePictureInGalleryUseCase
import ua.czrblz.domain.usecase.GetTopPostsUseCase

val domainModule = module {

    single<GetTopPostsUseCase> {
        GetTopPostsUseCase(
            get()
        )
    }

    single<SavePictureInGalleryUseCase> {
        SavePictureInGalleryUseCase(
            get(),
            get()
        )
    }
}