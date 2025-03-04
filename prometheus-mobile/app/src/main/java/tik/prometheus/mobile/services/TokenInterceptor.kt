package tik.prometheus.mobile.services

import okhttp3.Interceptor
import okhttp3.Response
import tik.prometheus.mobile.Configs

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header("Authorization", ("Bearer " + Configs.ACCESS_TOKEN) ?: "Bearer ").build();
        return chain.proceed(newRequest)
    }
}