package com.example.orthoepy.di

import com.example.orthoepy.MainActivity
import com.example.orthoepy.MainViewModel
import com.example.orthoepy.data.repository.DictionaryRepository
import com.example.orthoepy.fragmentcode.StoreFragment
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(mainViewModel: MainViewModel)

    fun inject(dictionaryRepository: DictionaryRepository)

}