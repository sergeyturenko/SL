package com.major.shop.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sergey on 09.01.14.
 */
public class DBHelper extends SQLiteOpenHelper implements IDBHelper{
  //=========================================================================
  protected static String  DATA_BASE = "ShopingDB";
  //=========================================================================
  //-------------------------------------------------------------------------
  public DBHelper(Context context) {
    super(context, DATA_BASE, null, 1);
  }
  //-------------------------------------------------------------------------
  @Override
  public void onCreate(SQLiteDatabase _db) {
	  _db.execSQL("create table "+TBL_CNT_NUMB+"(" + CNTP_NUMB_NAME +" text primary key, "+
	  		                                 CNT_NUMB_ID    +" integer  ) ");
      _db.execSQL("create table "+TBL_LISTS  +"( "+
                               LIST_ID       +" integer not null, " +
                               LIST_NAME     +" text    not null, " +
                               GLOBAL_LST_ID +" integer     null, " +
                               CREATE_DT     +" datetime        , " +
                               STATUS        +" char(1)         , " +//O-Open/X-Close
                               STATUS_DT     +" datetime        , " +
                               TYPE_LIST     +" char(1)         , " +
                              "primary key (LIST_ID) )" );
      _db.execSQL("create table "+TBL_PRODLST+" ( " +
                               LIST_ID       +" integer not null, " +
                               PROD_ID       +" integer not null, " +
                               PROD_NAME     +" text    not null, " +
                               PROD_CNT      +" integer     null, " +
                               UNIT_NAME     +" text        null, " +
                               COMMENT       +" text        null, " +
                               PROD_IS_BAY   +" char(1)     null, " +
                               DT_PRODUCT    +" datetime    null, " +
                               DT_BAY_PRODUCT+" datetime    null, " +
                              "primary key ("+LIST_ID+", "+PROD_ID+") "+
                              "FOREIGN KEY ("+LIST_ID+") REFERENCES "+TBL_LISTS+"("+LIST_ID+"))" );
      _db.execSQL("CREATE TABLE "+TBL_PROD+" (                             " +
                               PROD_ID       +" integer not null, " +
                               PROD_NAME     +" text    not null, " +
                               LANGUAGE      +" char(2)     null, " +
                               IS_ACTIVE     +" char(1)     null, " +
                               CH_DATE       +" date        null, " +
                              "primary key ("+PROD_ID+", " +PROD_NAME+") )" );
      _db.execSQL("CREATE table "+TBL_UNIT+" (                             " +
                               UNIT_ID       +" integer primary key autoincrement," +
                               UNIT_NAME     +" text,                             " +
                               LANGUAGE      +" char(2))                          " );
      _db.execSQL("create table "+TBL_UNITPROD+"(                          " +
                               PROD_ID       +" integer not null, " +
                               UNIT_ID       +" integer not null, " +
                              "FOREIGN KEY("+PROD_ID+") REFERENCES PROD("+PROD_ID+")," +
                              "FOREIGN KEY("+UNIT_ID+") REFERENCES UNIT("+UNIT_ID+") ) ");
  }
  //-------------------------------------------------------------------------
  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) { }
  //-------------------------------------------------------------------------
}
