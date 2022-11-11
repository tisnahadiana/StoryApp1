package id.tisnahadiana.storyapp.di


import android.content.Context
import id.tisnahadiana.storyapp.data.local.room.StoryDatabase
import id.tisnahadiana.storyapp.data.remote.retrofit.ApiConfig
import id.tisnahadiana.storyapp.data.repository.StoryRepository
import id.tisnahadiana.storyapp.data.repository.UserRepository
import id.tisnahadiana.storyapp.utils.AppExecutors

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        val storyDao = database.storyDao()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, storyDao, appExecutors)
    }

    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}