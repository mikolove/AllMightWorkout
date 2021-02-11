package com.mikolove.allmightworkout.oldCode

import androidx.lifecycle.ViewModelProvider
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FragmentFactoryModule {

/*    @Provides
    @Singleton
    fun provideFragmentFactory(viewModelFactory: ViewModelProvider.Factory, dateUtil : DateUtil) : BaseFragmentFactory{
        return BaseFragmentFactory(
            viewModelFactory = viewModelFactory,
            dateUtil = dateUtil
        )
    }*/
}
