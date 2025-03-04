package tik.prometheus.mobile.repository

import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tik.prometheus.mobile.Configs
import tik.prometheus.mobile.services.TokenAuthenticator
import tik.prometheus.mobile.services.TokenInterceptor

object RestServiceHelper {
    val baseUrl = Configs.configs?.restServiceHost + ":" + Configs.configs?.restServicePort

    private fun getRetrofit(): Retrofit {
        val dispatcher: Dispatcher = Dispatcher();
        dispatcher.maxRequests = 1

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(Configs.CONNECT_TIMEOUT)
            .authenticator(TokenAuthenticator())
            .addInterceptor(TokenInterceptor())
            .dispatcher(dispatcher)
            .build()
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun createApi(): RestServiceApi {
        return getRetrofit().create(RestServiceApi::class.java)
    }
}