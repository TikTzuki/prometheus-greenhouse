package tik.prometheus.mobile

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tik.prometheus.mobile.ui.adapters.ZLoadStateAdapter

abstract class ZPagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>,
) : PagingDataAdapter<T, VH>(diffCallback) {

    fun initAdapter(recyclerView: RecyclerView, btnRetry: Button, flow: Flow<PagingData<T>>, lifecycleScope: LifecycleCoroutineScope) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = withLoadStateFooter(
                footer = ZLoadStateAdapter { retry() }
            )
        }
        lifecycleScope.launch {
            flow.collectLatest {
                submitData(it)
            }
        }
        btnRetry.setOnClickListener {
            retry()
        }
    }

    fun addLoadStateListener(
        btnRetry: Button, progressBar: ProgressBar,
        handleErrorFunc: ((errorState: LoadState.Error) -> Unit)? = null
    ) {
        addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                btnRetry.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> {
                        btnRetry.visibility = View.VISIBLE
                        loadState.refresh as LoadState.Error
                    }

                    else -> null
                }
                errorState?.let {
                    handleErrorFunc?.invoke(it)
                }

            }
        }
    }

    private fun showToast(value: CharSequence) {
        Toast.makeText(null, value, Toast.LENGTH_LONG).show()
    }
}