package com.kamleshpractical.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.kamleshpractical.database.NoteDao
import com.kamleshpractical.database.NoteDatabase
import com.kamleshpractical.repository.AuthRepository
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
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepository(auth)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "notes_db"
        )
            .build()
    }

    @Provides
    fun provideNoteDao(db: NoteDatabase): NoteDao = db.noteDao()
}
