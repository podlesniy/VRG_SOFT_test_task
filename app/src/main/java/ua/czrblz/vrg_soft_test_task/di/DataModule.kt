package ua.czrblz.vrg_soft_test_task.di

import org.koin.dsl.module
import ua.czrblz.data.api.ApiService
import ua.czrblz.data.paging.PostsPagingSource
import ua.czrblz.data.repository.PictureRepositoryImpl
import ua.czrblz.data.repository.PostsRepositoryImpl
import ua.czrblz.domain.repository.PictureRepository
import ua.czrblz.domain.repository.PostsRepository
import ua.czrblz.network.NetworkAdapter

val dataModule = module {

    single<PostsRepository> {
        PostsRepositoryImpl(
            get()
        )
    }

    single<PictureRepository> {
        PictureRepositoryImpl()
    }

    single<PostsPagingSource> {
        PostsPagingSource(
            get()
        )
    }

    single<ApiService>() {
        NetworkAdapter().retrofit.create(ApiService::class.java)
    }
}