package com.example.ttscore.di

import com.example.ttscore.data.remote.TtscoreApi
import com.example.ttscore.data.repository.FriendshipRepositoryImpl
import com.example.ttscore.data.repository.MatchRepositoryImpl
import com.example.ttscore.data.repository.UserRepositoryImpl
import com.example.ttscore.domain.repository.FriendshipRepository
import com.example.ttscore.domain.repository.MatchRepository
import com.example.ttscore.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(api: TtscoreApi): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMatchRepository(api: TtscoreApi): MatchRepository {
        return MatchRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFriendshipRepository(api: TtscoreApi): FriendshipRepository {
        return FriendshipRepositoryImpl(api)
    }
}
