package com.example.note_app.activity

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note_app.R
import com.example.note_app.adapters.NoteAdapter
import com.example.note_app.adapters.updateHistoryAdapter
import com.example.note_app.databinding.ActivityAddScreenBinding
import com.example.note_app.databinding.ActivityMainBinding
import com.example.note_app.databinding.ActivityUpdateScreenBinding
import com.example.note_app.databinding.UpdateHistoryItemBinding
import com.example.note_app.helper.MyDBHelper
import com.example.note_app.interfaces.NoteInterface
import com.example.note_app.models.NoteModel
import com.example.note_app.models.UpdateHistoryModel
import java.util.*

private lateinit var binding: ActivityUpdateScreenBinding
class update_screen : AppCompatActivity() {
    lateinit var db: SQLiteDatabase;
    val updatehistoryList = mutableListOf<UpdateHistoryModel>();
    val cal = Calendar.getInstance();
    private var status:Int = 0;
    private lateinit var adapter: updateHistoryAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_screen)
//      hide bar
        supportActionBar?.hide();
        binding = ActivityUpdateScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      Receive data
        val iii = intent;
        val bundle = iii.extras;
        val myDBHelper = MyDBHelper(applicationContext);
        if(bundle != null){
            db = myDBHelper.readableDatabase;
//          set data for me.
            getUpdateByid(bundle.getInt("id"));
            Log.d("12345",bundle.getInt("id").toString());
            var rvUpdatehistory = binding.rvUpdateHistory;
            rvUpdatehistory.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            adapter = updateHistoryAdapter(updatehistoryList);
            rvUpdatehistory.adapter = adapter;
//      baseEvent
            setHandleBaseEvent(binding,bundle);


            setHandleClickUpdateBtn(binding,bundle);
        }
    }

    private fun setHandleClickUpdateBtn(binding: ActivityUpdateScreenBinding,bundle: Bundle){
        binding.btnUpdate.setOnClickListener{
            val ad = AlertDialog.Builder(this@update_screen);
            ad.setTitle("Xác nhận ");
            ad.setMessage("Bạn muốn thực hiện cập nhật này không ?");
            ad.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
                Log.d("test","test");
                val update_date = cal.get(Calendar.DAY_OF_MONTH).toString() + "-"+"${cal.get(Calendar.MONTH)+1}"+"-"+cal.get(
                    Calendar.YEAR)+" "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE);
                var data = UpdateHistoryModel(0,bundle.getInt("id"),binding.txtComment.text.toString(),update_date,bundle.getInt("status"),status)
                insertData(data,bundle);
                getUpdateByid(bundle.getInt("id"));
                adapter.reRender(updatehistoryList);

                val i = Intent(this,MainActivity::class.java);
                startActivity(i);
            })
            ad.setNegativeButton("Không"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss();
            }
            ad.show();
        }
    }

    private  fun insertData(data: UpdateHistoryModel,bundle: Bundle){
        val myDBHelper = MyDBHelper(applicationContext);
        val result = myDBHelper.insertUpdatehistory(data);

        val end_date = bundle.getString("end_date").toString();
        val updatedData = NoteModel(bundle.getInt("id"), bundle.getString("title").toString(),
            bundle.getString("desc").toString(),status,bundle.getString("start_date").toString(),end_date);
        editNote(updatedData);

        if(result.toInt() == -1)
            Toast.makeText(applicationContext,"Thêm ghi chú không thành công", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(applicationContext,"Thêm ghi chú thành công", Toast.LENGTH_SHORT).show();
    }

    private  fun  setHandleBaseEvent(binding: ActivityUpdateScreenBinding,bundle: Bundle){
        binding.btnBack.setOnClickListener{
            val i = Intent(this,MainActivity::class.java);
            startActivity(i);
        }
        binding.txtUpdatedTitle.setText(bundle.getString("title"));
        handleStatusEvent(binding,bundle);
    }

    private fun handleStatusEvent(binding: ActivityUpdateScreenBinding,bundle: Bundle){
        val pendingEle = binding.crvPending;
        val doingEle = binding.crvDoing;
        val completedEle = binding.crvCompleted;
        when(bundle.getInt("status")){
            0->{
                binding.crvPending.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));
                status = 0;
            }
            1->{
                binding.crvDoing.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));
                status = 1;
            }
            2->{
                binding.crvCompleted.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));
                status = 2;
            }
        }
        pendingEle.setOnClickListener{
            completedEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            doingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            pendingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));

            status = 0;
//          search
        }
        doingEle.setOnClickListener{
            completedEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            pendingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            doingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));

            status = 1;
//          search
        }
        completedEle.setOnClickListener{
            doingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            pendingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            completedEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));

            status = 2;
//          search
        }
    }

    private  fun editNote(data: NoteModel){
        val myDBHelper = MyDBHelper(applicationContext);
        val result = myDBHelper.updateNote(data);
        if(result == -1)
            Toast.makeText(applicationContext,"Chỉnh sửa ghi chú không thành công",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(applicationContext,"Chỉnh sửa ghi chú thành công",Toast.LENGTH_SHORT).show();
    }
    private fun getUpdateByid(id:Int){
        val myDBHelper = MyDBHelper(applicationContext);
        val cursor: Cursor = myDBHelper.getHistoryById(id);
        storeNotesInArray(cursor);
    }

    private fun storeNotesInArray(cursor: Cursor){
        updatehistoryList.clear();
        while (cursor.moveToNext()){
            Log.d("12345",cursor.getString(2)+"=")
            updatehistoryList.add(UpdateHistoryModel(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5)));
        }
    }


}