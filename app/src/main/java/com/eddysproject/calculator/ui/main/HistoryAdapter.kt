package com.eddysproject.calculator.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eddysproject.calculator.R
import com.eddysproject.calculator.db.data.History
import kotlinx.android.synthetic.main.view_history.view.*

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val histories = mutableListOf<History>()
    fun addItem(history: History) {
        histories.add(0, history)
        notifyDataSetChanged()
    }

    fun clearAll() {
        histories.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_history, parent,false)
        return HistoryViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(histories[position])

    }

    class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(history: History) {
            itemView.result.text = history.result
        }
    }
}