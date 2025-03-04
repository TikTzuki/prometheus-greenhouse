package tik.prometheus.mobile

interface NestableFragment<T> {
    fun insertNestedFragment(model: T)
}