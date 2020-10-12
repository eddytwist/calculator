package com.eddysproject.calculator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddysproject.calculator.R
import com.eddysproject.calculator.db.data.History
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val adapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    fun addToList(s: String) {
        adapter.addItem(History(s))
    }

    fun clearView() {
        adapter.clearAll()
    }
}