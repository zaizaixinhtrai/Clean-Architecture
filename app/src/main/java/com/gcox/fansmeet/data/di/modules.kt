package com.gcox.fansmeet.data.di

import com.gcox.fansmeet.data.di.NameInject.SCHEDULE_IO
import com.gcox.fansmeet.data.di.NameInject.SCHEDULE_UI
import com.gcox.fansmeet.data.repository.*
import com.gcox.fansmeet.data.repository.datasource.*
import com.gcox.fansmeet.data.repository.datasource.cloud.*
import com.gcox.fansmeet.data.repository.datasource.remote.CloudLoginDataSource
import com.gcox.fansmeet.domain.interactors.challenges.*
import com.gcox.fansmeet.domain.interactors.home.GetBannersUseCase
import com.gcox.fansmeet.domain.interactors.home.GetUsersUseCase
import com.gcox.fansmeet.domain.interactors.login.FacebookLoginUseCase
import com.gcox.fansmeet.domain.interactors.login.GoogleLoginUseCase
import com.gcox.fansmeet.domain.interactors.login.InstagramLoginUseCase
import com.gcox.fansmeet.domain.interactors.main.IAPIsfinishedCheckingUseCase
import com.gcox.fansmeet.domain.interactors.main.VerifyPurchaseTransactionUseCase
import com.gcox.fansmeet.domain.interactors.useraction.*
import com.gcox.fansmeet.domain.repository.*
import com.gcox.fansmeet.features.challenges.ChallengesViewModel
import com.gcox.fansmeet.features.flashscreen.SplashScreenViewModel
import com.gcox.fansmeet.features.home.HomeViewModel
import com.gcox.fansmeet.features.home.merchants.MerchantsViewModel
import com.gcox.fansmeet.features.login.LoginViewModel
import com.gcox.fansmeet.features.main.MainViewModel
import com.gcox.fansmeet.webservice.AppsterWebServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object NameInject {
    const val SCHEDULE_UI = "ui"
    const val SCHEDULE_IO = "io"
}

val appModules = listOf(
    module {
        single(SCHEDULE_UI) {
            AndroidSchedulers.mainThread()
        }
        single(SCHEDULE_IO) {
            Schedulers.io()
        }

        single {
            AppsterWebServices.get()
        }

//        single {
//            AppsterUtility.getAuth()
//        }
    },
    // login modules
    module {
        factory<LoginDataSource> { CloudLoginDataSource(get()) }
        factory<UserLoginRepository> { UserLoginDataRepository(get()) }
        factory { FacebookLoginUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { GoogleLoginUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { InstagramLoginUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { LoginViewModel(get(), get(), get()) }
    },

    // home modules
    module {
        factory<HomeDataSource> { CloudHomeDataSource(get()) }
        factory<HomeRepository> { HomeDataRepository(get()) }
        factory { GetUsersUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { GetBannersUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { HomeViewModel(get(), get()) }
    },

    // merchants modules
    module {
        viewModel { MerchantsViewModel(get(), get(), get(), get()) }
    },

    // home celebrity
    module {
        factory<CelebrityDataSource> { CloudCelebrityDataSource(get()) }
        factory<CelebrityRepository> { CelebrityDataRepository(get()) }
    },

    // user action
    module {
        factory<UserActionDataSource> { CloudUserActionDataSource(get()) }
        factory { FollowUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { LikeUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { UnlikeUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { UnFollowUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { ReportPostUserUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { ReportEntriesUserUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }

    },

    // challenge list
    module {
        factory<ChallengeDataSource> { CloudChallengeDataSource(get()) }
        factory<ChallengeRepository> { ChallengeDataRepository(get()) }
        factory { GetChallengeListEntriesUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { ChallengesViewModel(get()) }
    },

    // Splash modules
    module {
        viewModel { SplashScreenViewModel(get(), get(), get()) }
    },

    // Splash modules
    module {
        factory<MainDataSource> { CloudMainDataSource(get()) }
        factory<MainRepository> { MainDataRepository(get()) }
        factory { IAPIsfinishedCheckingUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { VerifyPurchaseTransactionUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }

        viewModel { MainViewModel(get(), get()) }
    }
)