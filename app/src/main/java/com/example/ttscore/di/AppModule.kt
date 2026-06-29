package com.example.ttscore.di

import androidx.room.Room
import com.example.ttscore.data.local.AppDatabase
import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.data.repository.FriendshipRepositoryImpl
import com.example.ttscore.data.repository.MatchRepositoryImpl
import com.example.ttscore.data.repository.UserRepositoryImpl
import com.example.ttscore.domain.repository.FriendshipRepository
import com.example.ttscore.domain.repository.MatchRepository
import com.example.ttscore.domain.repository.UserRepository
import com.example.ttscore.ui.viewmodel.CadastroViewModel
import com.example.ttscore.ui.viewmodel.FriendshipViewModel
import com.example.ttscore.ui.viewmodel.LoginViewModel
import com.example.ttscore.ui.viewmodel.MatchInProgressViewModel
import com.example.ttscore.ui.viewmodel.MatchViewModel
import com.example.ttscore.ui.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().userDao }
    single { get<AppDatabase>().matchDao }

    single { SessionManager(androidContext()) }

    single { com.example.ttscore.data.remote.RetrofitClient.api }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<MatchRepository> { MatchRepositoryImpl(get()) }
    single<FriendshipRepository> { FriendshipRepositoryImpl() }

    viewModel { CadastroViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { MatchViewModel(get(), get()) }
    viewModel { MatchInProgressViewModel(get(), get()) }
    viewModel { FriendshipViewModel(get()) }
}
