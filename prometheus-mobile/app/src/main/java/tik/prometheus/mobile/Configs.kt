package tik.prometheus.mobile

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import tik.prometheus.mobile.utils.Utils
import java.time.Duration
import java.util.concurrent.TimeUnit

object Configs {
    var TAG = Configs::class.java.toString()
    val CONNECT_TIMEOUT: Duration = Duration.ofSeconds(60);
    var ACCESS_TOKEN: String? = null
    private val CONFIG_URI: String = "http://192.168.1.3:8000"
    private val CONFIG_NAME: String = "prometheus-mobile"
    private val CONFIG_PROFILE: String = "dev"
    var configs: ConfigProperties? = null
    private var loaded: Boolean = false

    fun initConfig() {
        var client = OkHttpClient().newBuilder().callTimeout(60, TimeUnit.SECONDS).build()
        val builder = Retrofit.Builder()
            .baseUrl(CONFIG_URI)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        val retrofit = builder.build()
        val configService = retrofit.create(ConfigService::class.java)
        val result = configService.getConfigs(CONFIG_NAME, CONFIG_PROFILE).execute()
        configs = result.body()
        println(configs)
        loaded = true
    }

    internal interface ConfigService {
        @GET("/{name}-{profile}.json")
        fun getConfigs(@Path("name") name: String?, @Path("profile") profile: String?): Call<ConfigProperties>
    }

    class ConfigProperties(
        val brokerHost: String,
        val brokerPort: String,
        val restServiceHost: String,
        val restServicePort: String,
    ) {
        override fun toString(): String {
            return Utils.reflectionToString(this);
        }
    }
}



