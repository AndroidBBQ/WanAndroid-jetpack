package com.bbq.home.weight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bbq.home.R
import com.bbq.home.adapter.HomePageAdapter
import com.bbq.home.databinding.PagingLoadMoreBinding

class FooterAdapter(val adapter: HomePageAdapter) :
    LoadStateAdapter<FooterAdapter.BindingViewHolder>() {


    inner class BindingViewHolder(val binding: PagingLoadMoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: BindingViewHolder, loadState: LoadState) {
        val binding = holder.binding
        when (loadState) {
            is LoadState.Error -> {
                binding.loadMoreLoadCompleteView.visibility = View.GONE
                binding.loadMoreLoadEndView.visibility = View.GONE
                binding.loadMoreLoadingView.visibility = View.GONE
                binding.loadMoreLoadFailView.visibility = View.VISIBLE
                binding.loadMoreLoadFailView.setOnClickListener {
                    adapter.retry()
                }
            }
            is LoadState.Loading -> {
                binding.loadMoreLoadCompleteView.visibility = View.GONE
                binding.loadMoreLoadEndView.visibility = View.VISIBLE
                binding.loadMoreLoadingView.visibility = View.GONE
                binding.loadMoreLoadFailView.visibility = View.GONE
            }
            is LoadState.NotLoading -> {
                binding.loadMoreLoadCompleteView.visibility = View.GONE
                binding.loadMoreLoadEndView.visibility = View.GONE
                binding.loadMoreLoadingView.visibility = View.GONE
                binding.loadMoreLoadFailView.visibility = View.GONE
                //这里可以判断是否到底
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BindingViewHolder {
        return BindingViewHolder(
            DataBindingUtil.inflate<PagingLoadMoreBinding>(
                LayoutInflater.from(parent.context),
                R.layout.paging_load_more, parent, false
            )
        )
    }

}