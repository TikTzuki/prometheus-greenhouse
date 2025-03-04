package tik.prometheus.mobile.models

class AuthRequest(val username: String, val password: String) {
}

class AuthResponse(val token: String)