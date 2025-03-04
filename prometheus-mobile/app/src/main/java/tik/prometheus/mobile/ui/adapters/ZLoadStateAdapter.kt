package tik.prometheus.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import tik.prometheus.mobile.databinding.ItemPagingLoadingBinding

class ZLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ZLoadStateAdapter.LoadStateViewHolder>() {


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val process = holder.loadStateViewBinding.loadStateProgress
        val btnRetry = holder.loadStateViewBinding.loadStateRetry
        val txtErrorMessage = holder.loadStateViewBinding.loadStateErrorMessage
        btnRetry.isVisible = loadState !is LoadState.Loading
        txtErrorMessage.isVisible = loadState !is LoadState.Loading
        process.isVisible = loadState is LoadState.Loading
        if (loadState is LoadState.Error) {
            txtErrorMessage.text = loadState.error.localizedMessage
        }
        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            ItemPagingLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class LoadStateViewHolder(val loadStateViewBinding: ItemPagingLoadingBinding) :
        RecyclerView.ViewHolder(loadStateViewBinding.root)
}