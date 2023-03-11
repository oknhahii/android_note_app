package com.example.note_app.adapters

import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.R
import com.example.note_app.interfaces.NoteInterface
import com.example.note_app.models.NoteModel
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat

class NoteAdapter(var list:MutableList<NoteModel>,val onClickNote: NoteInterface): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    inner class NoteHolder(historyItemView: View): RecyclerView.ViewHolder(historyItemView);
    val _this = this;
    var listData = list;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false);
        return NoteHolder(view);
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
//      Set constants
        val sdf             = SimpleDateFormat("dd-MM-yyyy HH:mm",);
        val PENDING         = "Chưa thực hiện";
        val DOING           = "Đang thực hiện";
        val COMPLETED       = "Đã hoàn thành";
        val currentDate     = sdf.parse(sdf.format(System.currentTimeMillis()));
        val endDate         = sdf.parse(listData[position].end_day);
//      Set color of note,status
        val noteColors      = holder.itemView.context.resources.getIntArray(R.array.note_item_colors);
        val statusColors    = holder.itemView.context.resources.getIntArray(R.array.status_colors);

//      Set value for items.
        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.txtTitle).text = listData[position].title;
            holder.itemView.findViewById<TextView>(R.id.txtDesc).text = listData[position].desc;

            val status:Int = listData[position].status;
            val statusEle = holder.itemView.findViewById<TextView>(R.id.txtStatus);
            when(status){
                0  -> {
                    statusEle.text = PENDING;
                };
                1  -> {
                    statusEle.text = DOING
                }
                2  ->{
                    statusEle.text = COMPLETED
                }
            }
            statusEle.setTextColor(statusColors[status])

            holder.itemView.findViewById<TextView>(R.id.txtStartDate).text = listData[position].start_day;
            holder.itemView.findViewById<TextView>(R.id.txtEndDate).text = listData[position].end_day;
            holder.itemView.findViewById<CardView>(R.id.noteItem).setCardBackgroundColor(noteColors[position%5]);
            val diff =  endDate.time - currentDate.time;
            Log.d("12345",currentDate.toString());
            Log.d("12345",endDate.toString());
            Log.d("12345",diff.toString());

            Log.d("status",status.toString())
            Log.d("status",(status!=2.toInt()).toString());
            if((diff <= 0.toLong()) && (status != 2.toInt())){
                holder.itemView.findViewById<FloatingActionButton>(R.id.notificationIcon).visibility = View.VISIBLE;
            }

//          Handle click event
            holder.itemView.setOnClickListener{
                onClickNote.onClick(position);
            }
//          Hanle long click event
            holder.itemView.setOnLongClickListener{
                onClickNote.onLongClick(position);
                return@setOnLongClickListener true
            }

        };
    }

    override fun getItemCount(): Int {
        return listData.size;
    };

    fun reRender(items: MutableList<NoteModel>){
        listData = items;
        notifyDataSetChanged();
    }
}