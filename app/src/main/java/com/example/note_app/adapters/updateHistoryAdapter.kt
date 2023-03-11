package com.example.note_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.R
import com.example.note_app.models.NoteModel
import com.example.note_app.models.UpdateHistoryModel

class updateHistoryAdapter(val list:MutableList<UpdateHistoryModel>):RecyclerView.Adapter<updateHistoryAdapter.UpdatehistroyHolder>() {
    inner class UpdatehistroyHolder(historyItemView: View):RecyclerView.ViewHolder(historyItemView)
    private var listData = list;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatehistroyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.update_history_item,parent,false);
        return UpdatehistroyHolder(view);
    }

    override fun onBindViewHolder(holder: UpdatehistroyHolder, position: Int) {
        var status = mutableListOf<String>();
        status.add("Chưa thực hiện");
        status.add("Đang thực hiện");
        status.add("Đã hoàn thành");


        holder.itemView.apply {
            val noteColors      = holder.itemView.context.resources.getIntArray(R.array.note_item_colors);
            holder.itemView.findViewById<TextView>(R.id.txtUpdateDate).text = listData[position].update_day;
            holder.itemView.findViewById<TextView>(R.id.txtStatusOne).text = status[ listData[position].status_one];
            holder.itemView.findViewById<TextView>(R.id.txtStatusTwo).text = status[listData[position].status_two];
            val statusColor = resources.getIntArray(R.array.status_colors);
            holder.itemView.findViewById<TextView>(R.id.txtStatusOne).setTextColor(statusColor[listData[position].status_one]);
            holder.itemView.findViewById<TextView>(R.id.txtStatusTwo).setTextColor(statusColor[listData[position].status_two]);

            holder.itemView.findViewById<TextView>(R.id.txtUpdateComment).text = listData[position].comment;
            holder.itemView.findViewById<LinearLayout>(R.id.history_item).setBackgroundColor(noteColors[position%5]);
        }
    }

    override fun getItemCount(): Int {
        return listData.size;
    };

    fun reRender(items: MutableList<UpdateHistoryModel>){
        listData = items;
        notifyDataSetChanged();
    }
}