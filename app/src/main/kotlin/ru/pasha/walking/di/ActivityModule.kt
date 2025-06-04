package ru.pasha.walking.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.pasha.common.di.ActivityScope
import ru.pasha.feature.map.api.StepController

@Module
interface ActivityModule {
    companion object {
        @Provides
        @ActivityScope
        fun provideStepsListener(context: Context) =
            StepController(context)
    }
}
