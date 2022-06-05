package com.shoshin.logincalc.di

import android.content.Context
import android.content.SharedPreferences
import com.shoshin.logincalc.core.storage.AppStorage
import com.shoshin.logincalc.core.storage.AuthStorage
import com.shoshin.logincalc.core.storage.AuthStorageImpl
import com.shoshin.logincalc.core.storage.SharedPreferencesWrapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [AppModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("CALC_SHARED_PREFERENCES", Context.MODE_PRIVATE)

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        @Singleton
        abstract fun bindAppStorage(impl: SharedPreferencesWrapper): AppStorage

        @Binds
        @Singleton
        abstract fun bindAuthStorage(impl: AuthStorageImpl): AuthStorage
    }
}