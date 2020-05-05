package com.example.kkon

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class BoardDataAdapter (var arr: ArrayList<BoardData>):
    RecyclerView.Adapter<BoardDataAdapter.ViewHolder>() {

    // 따로 만들어줘야 listview 처럼 사용 가능, 그리고 ViewHolder의 init block에서 초기화 해줘야함!!!
    interface OnItemClickListener {
        fun OnItemClick (holder: ViewHolder, view: View, data: BoardData, position: Int)
    }
    var itemClickListener:OnItemClickListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.layout_board, p0, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.vTitle.text = arr[p1].dTitle.toString()
        p0.vDate.text = arr[p1].dDate.toString()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var vTitle: TextView
        var vDate: TextView

        init {
            vTitle = itemView.findViewById(R.id.txt_title)
            vDate = itemView.findViewById(R.id.txt_date)

            itemView.setOnClickListener {
                val position = adapterPosition
                itemClickListener?.OnItemClick(this, it, arr[position], position) }
        }
    }

}
