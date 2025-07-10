package com.example.tarlauygulamasi.di

import android.content.Context
import com.example.tarlauygulamasi.data.dao.UserDao
import com.example.tarlauygulamasi.data.database.UserDatabase
import com.example.tarlauygulamasi.data.repository.AuthenticationRepositoryImpl
import com.example.tarlauygulamasi.data.repository.UserRepositoryImpl
import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.domain.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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


}