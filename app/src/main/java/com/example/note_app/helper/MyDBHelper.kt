package com.example.note_app.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.note_app.models.NoteModel
import com.example.note_app.models.UpdateHistoryModel
import java.text.SimpleDateFormat
import java.util.Date

class MyDBHelper(context :Context): SQLiteOpenHelper(context,"note_app_db",null,1) {
        private val context = context;
//       Khi ma db khong ton tai. Thi ta onCreate dc chay. Thi ta sex tao ra cac bang can thiet.
        val _ID         =   "_id";
        //      Note table constant.
        val NOTE        =   "note";
        val TITLE       =   "title";
        val STATUS      =   "status";
        val START_DAY  =   "start_day";
        val END_DAY     =   "end_day";
        //      History table constant.
        val UPDATE_HISTORY: String = "update_histrory";
        val NOTE_ID     =   "note_id";
        val COMMENT     =   "comment";
        val UPDATE_DAY  =   "update_day";
        val STATUS_ONE  =   "status_one";
        val STATUS_TWO  =   "status_two";
        override fun onCreate(db: SQLiteDatabase?) {


        val CREATE_NOTE_QUERY = "CREATE TABLE  ${NOTE}(\n" +
                "    ${_ID}         INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    ${TITLE}       TEXT        NOT NULL,\n" +
                "    des            TEXT        NOT NULL,\n" +
                "    ${STATUS}      INT         NOT NULL,\n" +
                "    ${START_DAY}   DATETIME    NOT NULL,\n" +
                "    ${END_DAY}     DATETIME    NOT NULL\n"  +
                ");"

        val CREATE_HISTORY_TABLE = "CREATE TABLE ${UPDATE_HISTORY} (\n" +
                "    ${_ID}         INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    ${NOTE_ID}     INTEGER     NOT NULL,\n" +
                "    ${COMMENT}     TEXT        NOT NULL,\n" +
                "    ${UPDATE_DAY}  DATETIME    NOT NULL,\n" +
                "    ${STATUS_ONE}  INTEGER     NOT NULL,\n" +
                "    ${STATUS_TWO}  INTEGER     NOT NULL\n" +
                ");"
//exec query
        db?.execSQL(CREATE_NOTE_QUERY);
        db?.execSQL(CREATE_HISTORY_TABLE);

        }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
//        update thi khong lam gi het
    }

    fun readAllData():Cursor{
        val query = "SELECT * FROM " + NOTE;
        val db = this.readableDatabase;

        lateinit var cursor: Cursor;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    fun searchData(param: String?):Cursor{
        lateinit var searchData: String;
        if(!(param is String)) searchData = "";
        else searchData = param;
        val query = "select * from note where title like  '%${searchData}%' or des like '%${searchData}%'";
        val db = this.readableDatabase;
        lateinit var cursor: Cursor;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return  cursor;
    }

    fun  deleteOneRow(row_id: Int):Int{
        val db              = this.writableDatabase;
        val noteTable       = db.delete(NOTE,"_id=?", arrayOf(row_id.toString()));
        val historyTable    =  db.delete(UPDATE_HISTORY,"${NOTE_ID}=?", arrayOf(row_id.toString()));
       return noteTable + historyTable;
    }

    fun addNote(data : NoteModel):Long{
        val db  = this.writableDatabase;
        val cv  = ContentValues();

        cv.put(TITLE, data.title);
        cv.put("des", data.desc);
        cv.put(STATUS, data.status);
        cv.put(START_DAY,data.start_day);
        cv.put(END_DAY,data.end_day);

        val result = db.insert(NOTE,null, cv);
        return result;
    }

    fun updateNote(note : NoteModel):Int{
        val db  = this.writableDatabase;
        val cv  = ContentValues();
        Log.d("12345",note.id.toString());
        cv.put(TITLE, note.title);
        cv.put("des", note.desc);
        cv.put(STATUS, note.status);
        cv.put(START_DAY,note.start_day);
        cv.put(END_DAY,note.end_day);

        val result = db.update(NOTE,cv,"_id=?", arrayOf(note.id.toString()));
        return result;
    }

    fun insertUpdatehistory(data : UpdateHistoryModel):Long{
        val db  = this.writableDatabase;
        val cv  = ContentValues();

        cv.put(NOTE_ID, data.note_id);
        cv.put(COMMENT, data.comment);
        cv.put(UPDATE_DAY, data.update_day);
        cv.put(STATUS_ONE,data.status_one);
        cv.put(STATUS_TWO,data.status_two);

        val result = db.insert(UPDATE_HISTORY,null, cv);
        return result;
    }
    fun getHistoryById(id: Int):Cursor{
        val query: String = "SELECT * FROM update_histrory where note_id = ${id}";

        val db = this.readableDatabase;

        lateinit var cursor: Cursor;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    fun filterNoteByStatus(status: Int):Cursor{
        val query: String = "SELECT * FROM note where status = ${status}";

        val db = this.readableDatabase;

        lateinit var cursor: Cursor;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}