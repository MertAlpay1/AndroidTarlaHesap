package com.example.tarlauygulamasi.di

import android.content.Context
import com.example.tarlauygulamasi.data.dao.UserDao
import com.example.tarlauygulamasi.data.database.UserDatabase
import com.example.tarlauygulamasi.data.locale.dao.FieldDao
import com.example.tarlauygulamasi.data.locale.database.FieldDatabase
import com.example.tarlauygulamasi.data.repository.AuthenticationRepositoryImpl
import com.example.tarlauygulamasi.data.repository.FieldRepositoryImpl
import com.example.tarlauygulamasi.data.repository.UserRepositoryImpl
import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import com.example.tarlauygulamasi.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        userRepository: UserRepository
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(auth, userRepository)
    }

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getInstance(context)
    }
    @Provides
    @Singleton
    fun provideUserDao(db: UserDatabase): UserDao= db.userDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideFieldDatabase(@ApplicationContext context: Context): FieldDatabase{
        return FieldDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideFieldDao(db: FieldDatabase): FieldDao=db.fieldDao()

    @Singleton
    @Provides
    fun provedeFieldRepository(fieldDao: FieldDao): FieldRepository{
        return FieldRepositoryImpl(fieldDao)
    }




}