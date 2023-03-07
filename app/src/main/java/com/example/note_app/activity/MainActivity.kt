package com.example.note_app.activity

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note_app.R
import com.example.note_app.adapters.NoteAdapter
import com.example.note_app.databinding.ActivityMainBinding
import com.example.note_app.databinding.CustomAlertdilaogBinding
import com.example.note_app.helper.MyDBHelper
import com.example.note_app.interfaces.NoteInterface
import com.example.note_app.models.NoteModel

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
//    Bien quan ly database
    lateinit var db: SQLiteDatabase;
//    con tro quan ly truy van trong db;
    lateinit var rs: Cursor;
    val noteList = mutableListOf<NoteModel>();
    lateinit var adapter : NoteAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      hide bar
        supportActionBar?.hide();
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myDBHelper = MyDBHelper(applicationContext);
        db = myDBHelper.readableDatabase;
        rs = db.rawQuery("Select * from note",null);
//      Read data from sqlite to data array.
        getAllData();
//      set adapter
        val rsNotes =  binding.rsNotes;
        adapter = NoteAdapter(noteList,object: NoteInterface{
            override fun onClick(pos: Int) {
                navigatetion(pos);
            }
            override fun onLongClick(pos: Int) {
               deleteItem(pos);
            }
        });
//        setting for render recycler view to UI
        rsNotes.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rsNotes.adapter = adapter;

//       Search listener
         setSearchListener(binding);
//       Filter with status
         setFilterEvent(binding);
//       handle click add btn
         setAddEvent(binding);

//        Register contextmenu
//        registerForContextMenu(binding.rsNotes);
    }

    fun navigatetion(pos:Int){
        val build           = AlertDialog.Builder(this);
        val dialogBinding   = CustomAlertdilaogBinding.inflate(LayoutInflater.from(this));
        build.setView(dialogBinding.root);
        val item            = noteList[pos];
        val id              = item.id;
        val title           = item.title;
        val desc            = item.desc;
        val start_date      = item.start_day;
        val end_date        = item.end_day;
        val status          = item.status;

        dialogBinding.btnUpdate.setOnClickListener{
//          Update btn.
            val intentValue = Intent(this,update_screen::class.java);
            val bundle = Bundle();
            bundle.putInt("id",id);
            bundle.putString("title",title);
            bundle.putString("desc",desc);
            bundle.putString("start_date",start_date);
            bundle.putString("end_date",end_date);
            bundle.putInt("status",status);
            intentValue.putExtras(bundle);
            startActivity(intentValue);
        }
        dialogBinding.btnEdit.setOnClickListener{
//          Edit btn.
            val i = Intent(this,add_screen::class.java);
            var bundle = Bundle();
            bundle.putInt("id",id);
            bundle.putString("title",title);
            bundle.putString("desc",desc);
            bundle.putString("start_date",start_date);
            bundle.putString("end_date",end_date);
            bundle.putInt("status",status);

            i.putExtras(bundle);
            startActivity(i);
        }

        val dialog = build.create();
        dialog.show();
    }

    fun setAddEvent(binding: ActivityMainBinding){
       binding.btnAdd.setOnClickListener {
           val i = Intent(this,add_screen::class.java);
           startActivity(i);
       }
    }

    fun setSearchListener(binding: ActivityMainBinding){
        binding.srSearchBar.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(param: String?): Boolean {
                searchData(param);
                adapter.reRender(noteList);
                binding.crvCompleted.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
                binding.crvDoing.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
                binding.crvPending.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
                return  true;
            }
        })
    }

    private fun setFilterEvent(binding: ActivityMainBinding){
        val pendingEle = binding.crvPending;
        val doingEle = binding.crvDoing;
        val completedEle = binding.crvCompleted;
        pendingEle.setOnClickListener{
            completedEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            doingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            pendingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));
            filterByStatus(0);
            adapter.reRender(noteList);
//          search
        }
        doingEle.setOnClickListener{
            completedEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            pendingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            doingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));
            filterByStatus(1);
            adapter.reRender(noteList);
//          search
        }
        completedEle.setOnClickListener{
            doingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            pendingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
            completedEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_focus));
//          search
            filterByStatus(2);
            adapter.reRender(noteList);
        }
    }

    fun deleteItem(pos: Int){
        val ad = AlertDialog.Builder(this@MainActivity);
        ad.setTitle("Xoá");
        ad.setMessage("Bạn muốn xóa ghi chú này không ?");
        ad.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
            try {
                val helper = MyDBHelper(applicationContext);
                var result = helper.deleteOneRow(noteList[pos].id);
                if(result == -1){
                    Toast.makeText(applicationContext,"Xóa ghi chú không thành công",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(applicationContext, "Xóa ghi chú thành công.", Toast.LENGTH_SHORT).show();
                    getAllData();
//                  Update view is did in adapter.
                    adapter.reRender(noteList);
                }
            }catch (e: java.lang.Exception){
                Log.e("error",e.toString());
            }
        })
        ad.setNegativeButton("Không"){ dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss();
        }
        ad.show();
    }

    private fun storeNotesInArray(cursor: Cursor){
        noteList.clear();
        while (cursor.moveToNext()){
            noteList.add(NoteModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4),cursor.getString(5)));
        }
    }

    private fun getAllData(){
        val myDBHelper = MyDBHelper(applicationContext);
        val cursor: Cursor = myDBHelper.readAllData();
        storeNotesInArray(cursor);
    }

    private fun searchData(param:String?){
        val myDBHelper = MyDBHelper(applicationContext);
        val cursor: Cursor = myDBHelper.searchData(param);
        storeNotesInArray(cursor);
    }

    private fun filterByStatus(param:Int){
        val myDBHelper = MyDBHelper(applicationContext);
        val cursor: Cursor = myDBHelper.filterNoteByStatus(param);
        storeNotesInArray(cursor);
    }
}