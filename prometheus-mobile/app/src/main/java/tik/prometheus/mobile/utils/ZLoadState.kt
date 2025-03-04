package tik.prometheus.mobile.utils

sealed class ZLoadState {
    class Loading : ZLoadState() {

    }

    class NotLoading : ZLoadState() {

    }

    class Error(val error: Throwable) : ZLoadState()
    companion object {
        val LOADING = Loading()
        val NOT_LOADING = NotLoading()
    }
}
