package app.sonlabs.myweatherforecast.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.sonlabs.myweatherforecast.BR
import app.sonlabs.myweatherforecast.R
import app.sonlabs.myweatherforecast.data.remote.Current

class DetailAdapter(val isMetric: Boolean) :
    ListAdapter<Current, DetailViewHolder>(DataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_detail_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: DetailViewHolder, position: Int) {
        viewHolder.bind(getItem(position), isMetric)
    }

    private class DataDiffCallback : DiffUtil.ItemCallback<Current>() {
        override fun areItemsTheSame(oldItem: Current, newItem: Current) =
            oldItem.dt == newItem.dt

        override fun areContentsTheSame(oldItem: Current, newItem: Current) =
            oldItem == newItem
    }
}

class DetailViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(detail: Current, isMetric: Boolean) {
        binding.setVariable(BR.current, detail)
        binding.setVariable(BR.isMetric, isMetric)
        binding.executePendingBindings()
    }
}
