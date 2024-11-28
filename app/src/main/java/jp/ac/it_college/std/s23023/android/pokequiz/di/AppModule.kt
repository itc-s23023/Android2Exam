package jp.ac.it_college.std.s23023.android.pokequiz.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class) // SingletonComponent にインストール
object AppModule {

    @Provides
    @Singleton
    fun provideGenerationId(): Int {
        return 1 // 適切な値を設定
    }
}
