package com.example.note_app.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.note_app.R
import com.example.note_app.databinding.ActivityAddScreenBinding
import com.example.note_app.databinding.ActivityMainBinding
import com.example.note_app.helper.MyDBHelper
import com.example.note_app.models.NoteModel
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.log

private lateinit var binding: ActivityAddScreenBinding
@Suppress("DEPRECATION")
class add_screen : AppCompatActivity() {
    lateinit var db: SQLiteDatabase;
    private var status: Int = 0;
    val cal = Calendar.getInstance();
    lateinit var date :String;
    lateinit var time :String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_screen)
        val mySef = SimpleDateFormat("dd-MM-yy");
        date = cal.get(Calendar.DAY_OF_MONTH).toString() + "-"+"${cal.get(Calendar.MONTH)+1}"+"-"+cal.get(Calendar.YEAR);
        time = cal.get(Calendar.HOUR).toString()+":"+cal.get(Calendar.MINUTE);
//      hide bar
        supportActionBar?.hide();

        binding = ActivityAddScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val iii = intent;
        val bundle = iii?.extras;
        if(bundle != null){
            val sdfDate = SimpleDateFormat("dd-MM-yyyy");
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm");
            val realDate =  sdfDate.parse(bundle.getString("end_date"));
            val fullDate =  sdf.parse(bundle.getString("end_date"));
            date = sdfDate.format(realDate);
            time = fullDate.hours.toString() + ":" + fullDate.minutes.toString();
            status = bundle.getInt("status");
            Log.d("12345",time);
            Log.d("12345",date);

//            fill data to form
            fillDataToForm(binding,bundle);
//            update edited data
           binding.btnAdd.setOnClickListener{
               val end_date = date + " " + time;
               Log.d("12345",bundle.getString("start_date").toString());
               val updatedData = NoteModel(bundle.getInt("id"), binding.txtTitle.text.toString(),
                   binding.txtContent.text.toString(),status,bundle.getString("start_date").toString(),end_date);
               editNote(updatedData);

            val ii = Intent(this,MainActivity::class.java);
            startActivity(ii);
           }
        }else{
//          insert case
            setHandleClickAddBtn(binding);
        }

//      baseEvent
        setHandleBaseEvent(binding);
    }

    private fun fillDataToForm(binding: ActivityAddScreenBinding,bundle: Bundle){
        binding.txtTitle.setText(bundle.getString("title").toString());
        binding.txtScreenTitle.setText("Chỉnh sửa ghi chú ");
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
        binding.txtDate.visibility = View.VISIBLE;
        binding.txtTime.visibility = View.VISIBLE;

        binding.txtDate.setText(date);
        binding.txtTime.setText(time);

        binding.txtContent.setText(bundle.getString("desc"))
        binding.btnAdd.setImageResource(R.drawable.ic_baseline_done_24);
    }

    private  fun  setHandleBaseEvent(binding: ActivityAddScreenBinding){
            binding.btnBack.setOnClickListener{
                val i = Intent(this,MainActivity::class.java);
                startActivity(i);
            }
            handleSelectStatus(binding);
            handleSelectDate(binding);
            handleSelectTime(binding);
    }

    private fun setHandleClickAddBtn(binding: ActivityAddScreenBinding){
        binding.btnAdd.setOnClickListener{
            val ad = AlertDialog.Builder(this@add_screen);
            ad.setTitle("Xác nhận ");
            ad.setMessage("Bạn muốn thực hiện chỉnh sửa không ?");
            ad.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
                Log.d("test","test");
                val end_date = date + " " + time;
                val start_date = cal.get(Calendar.DAY_OF_MONTH).toString() + "-"+"${cal.get(Calendar.MONTH)+1}"+"-"+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE);
                var note = NoteModel(0,binding.txtTitle.text.toString(),binding.txtContent.text.toString(),status,start_date,end_date);
                insertData(note);
                refreshForm(binding);
            })
            ad.setNegativeButton("Không"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss();
            }
            ad.show();
        }
    }

    private fun refreshForm(binding: ActivityAddScreenBinding){
        binding.txtTitle.setText("");
        binding.txtTitle.requestFocus();
        val pendingEle = binding.crvPending;
        val doingEle = binding.crvDoing;
        val completedEle = binding.crvCompleted;


        completedEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
        doingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));
        pendingEle.setCardBackgroundColor(resources.getColor(R.color.status_bgr_normal));

        binding.txtDate.visibility = View.GONE;
        binding.txtTime.visibility = View.GONE;

        binding.txtContent.setText("");

    }

    private fun handleSelectStatus(binding: ActivityAddScreenBinding){
            val pendingEle = binding.crvPending;
            val doingEle = binding.crvDoing;
            val completedEle = binding.crvCompleted;
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

    private fun handleSelectDate(binding: ActivityAddScreenBinding){
        binding.iconCalender.setOnClickListener{
            val dateEle = binding.txtDate;
            val cal = Calendar.getInstance();

            var day = cal.get(Calendar.DAY_OF_MONTH);
            var month = cal.get(Calendar.MONTH);
            var year    = cal.get(Calendar.YEAR);
            DatePickerDialog(this,DatePickerDialog.OnDateSetListener{
                view,sYear,sMonth,sDayOfMonth ->
                Log.d("11111",sDayOfMonth.toString()+"-"+sMonth.toString()+"-"+sYear.toString());
                dateEle.text = "${sDayOfMonth}-${sMonth + 1}-${sYear}"
                dateEle.visibility = View.VISIBLE;
                date = "${sDayOfMonth}-${sMonth + 1}-${sYear}";
                Log.d("11111",date);
            },year,month,day).show();
        }
    }

    private fun  handleSelectTime(binding: ActivityAddScreenBinding){
        val timeIconEle = binding.iconTime;
        timeIconEle.setOnClickListener{
            val txtTimeEle = binding.txtTime;
            val cal = Calendar.getInstance();

            var hour = cal.get(Calendar.HOUR);
            var minute = cal.get(Calendar.MINUTE);

            TimePickerDialog(this,TimePickerDialog.OnTimeSetListener{
                view,sHourOfDay,sMinute->
                txtTimeEle.text = "${sHourOfDay}:${sMinute}";
                txtTimeEle.visibility = View.VISIBLE;
                time =  "${sHourOfDay}:${sMinute}";
                Log.d("11111",time);
            },hour,minute,true).show();
        }
    }

    private fun insertData(data: NoteModel){
        val myDBHelper = MyDBHelper(applicationContext);
        val result = myDBHelper.addNote(data);
        if(result.toInt() == -1)
            Toast.makeText(applicationContext,"Thêm ghi chú không thành công",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(applicationContext,"Thêm ghi chú thành công",Toast.LENGTH_SHORT).show();
    }

    private  fun editNote(data: NoteModel){
        val myDBHelper = MyDBHelper(applicationContext);
        val result = myDBHelper.updateNote(data);
        if(result == -1)
            Toast.makeText(applicationContext,"Chỉnh sửa ghi chú không thành công",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(applicationContext,"Chỉnh sửa ghi chú thành công",Toast.LENGTH_SHORT).show();
    }
}