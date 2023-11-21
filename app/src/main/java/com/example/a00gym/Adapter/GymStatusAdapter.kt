package com.example.a00gym.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a00gym.DataClass.GymStatus
import com.example.a00gym.R

class GymStatusAdapter(private val gymStatusList: List<GymStatus>) :
    RecyclerView.Adapter<GymStatusAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDateTime: TextView = itemView.findViewById(R.id.textDateTime)
        // 다른 뷰들을 여기에 추가할 수 있습니다.
    }

    // 아이템 클릭 리스너 인터페이스
    interface OnItemClickListener {
        fun onItemClick(gymStatus: GymStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gym_status, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gymStatus = gymStatusList[position]

        // 데이터를 뷰에 바인딩
        holder.textDateTime.text = gymStatus.dateTime
        // 다른 데이터를 뷰에 바인딩할 수 있습니다.
        Log.d("YourActivityTag", "ItemCount: ${gymStatusList.size}")

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(gymStatus)
        }
    }


    override fun getItemCount(): Int {
        return gymStatusList.size
    }


    // OnItemClickListener 변수 선언 및 설정 메서드
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}