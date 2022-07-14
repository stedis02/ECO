package com.example.tpueco.DI

import android.content.Context
import com.example.tpueco.MainActivity
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.presentation.CameraActivity
import com.example.tpueco.presentation.fragment.DocumentCameraFragment
import com.example.tpueco.presentation.fragment.MailMainFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import kotlin.properties.Delegates.notNull

@Component(modules = [AppModule::class])
interface AppComponent : DocumentDeps{
    fun inject(activity: MainActivity)
    fun inject(activity: CameraActivity)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

}

@Module
class AppModule{

    @Provides
    fun provideUsersAPI(): UsersAPI{
        var httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oauth.tpu.ru/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(UsersAPI::class.java)
    }

}

@Component(modules = [DocumentFeatureModule::class], dependencies = [DocumentDeps::class])
internal interface DocumentFeatureComponent{
    fun inject(fragment: DocumentCameraFragment)
    fun inject(fragment: MailMainFragment)

    @Component.Builder
    interface Builder{
        fun deps(deps: DocumentDeps): Builder
        fun build(): DocumentFeatureComponent

    }
}
@Module
class DocumentFeatureModule{


}

interface DocumentDeps{

}

interface DocumentDepsProvider{
    val deps: DocumentDeps
    companion object : DocumentDepsProvider by DocumentDepsStore
}

object  DocumentDepsStore : DocumentDepsProvider{
    override var deps: DocumentDeps by notNull()
}