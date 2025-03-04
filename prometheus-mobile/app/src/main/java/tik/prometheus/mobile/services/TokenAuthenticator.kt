package tik.prometheus.mobile.services

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val accessToken = ""
        return response.request().newBuilder().header("Authorization", accessToken).build()
    }
}