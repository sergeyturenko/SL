package com.major.shop.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.major.shop.common.DBHelper;
import com.major.shop.common.IDBHelper;
import com.major.shop.common.SM;
import com.major.shop.model.BList;
import com.major.shop.model.BProduct;
import com.major.shop.model.BProducts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sergey on 11.01.14.
 */
public class ListOperLogic implements IDBHelper {
  //=================================================================
  protected SQLiteDatabase db     = null;
  protected Context        context= null;
  //=================================================================
  //-----------------------------------------------------------------
  public ListOperLogic(Context _context){
    context = _context;
    db      = DBHelper.getDataBaseConnection();
  }
  //-----------------------------------------------------------------
  public ArrayList <BList> getAllLists(String _modeLoad){
    ArrayList <BList> list = new ArrayList<BList>();
    BList     listItem = null;
    BList     bl       = null;
    BProducts prds     = null;
    Cursor c = db.rawQuery("SELECT " + LIST_ID + " as _id, " + LIST_NAME + ", " + GLOBAL_LST_ID + ", " +
                                       CREATE_DT + ", " + STATUS + ", " + STATUS_DT + ", " + TYPE_LIST +
                           "  from " + TBL_LISTS + " order by _id desc ", null);
    try{
      if (c!= null && c.getCount() > 0){
        c.moveToFirst();
        do {
          listItem = new BList();
          listItem.listId       = c.getInt   (c.getColumnIndex(_ID          ));
          listItem.listName     = c.getString(c.getColumnIndex(LIST_NAME    ));
          listItem.globalListId = c.getInt   (c.getColumnIndex(GLOBAL_LST_ID));
          listItem.createDate   = c.getString(c.getColumnIndex(CREATE_DT    ));
          listItem.status       = c.getString(c.getColumnIndex(STATUS       ));
          listItem.statusDate   = c.getString(c.getColumnIndex(STATUS_DT    ));
          listItem.typeList     = c.getString(c.getColumnIndex(TYPE_LIST    ));
        
          list.add(listItem);
        } while (c.moveToNext());
        if((list != null && list.size()>0) && 
           (SM.isEmpty(_modeLoad)          || 
            "F".equalsIgnoreCase(_modeLoad)||
            "G".equalsIgnoreCase(_modeLoad))){
//          if(!c.isClosed()) c.close ();
//          if(db.isOpen  ()) db.close();
//          for(int i=0; i<list.size();i++){
//        	bl = list.get(i);
//        	prds = new ProductOperationLogic(context).getProductsLst(bl.listId);
//        	bl.strProdFromLst = getStrFormProdRow(prds);
//            bl.products = prds;
//          }
        }
      }
    }
    finally{ if(!c.isClosed())c.close();}
    return list;
  }
  //-----------------------------------------------------------------
  public String getStrFormProdRow(BProducts _prds){
	BProduct prod = null;
    StringBuilder sb = new StringBuilder();
    if(_prds!= null && _prds.products!= null && _prds.products.size()>0){
      for(int i=0; i<_prds.products.size();i++){
        prod = _prds.products.get(i);
        sb.append(prod.productName+" ("+prod.productCnt+" "+prod.unitName+" "+prod.comment+"); ");
      }
    }
	return sb.toString();
  }
  //-----------------------------------------------------------------
  public int getMaxListId(){
    int maxId = 0;
    Cursor c = db.rawQuery("SELECT max(" + LIST_ID + ") as _id " + 
                           "  from " + TBL_LISTS , null);
    if (c!= null && c.getCount() > 0){
      c.moveToFirst();
      do {
        maxId = c.getInt(c.getColumnIndex(_ID));
      } while (c.moveToNext());
    }
    return maxId;
  }
  //-----------------------------------------------------------------
    public BList getListById(int _idList){
      BList lst = new BList();
      Cursor c  = null;
      try{
        c = db.rawQuery(" select " + LIST_ID + " , " + LIST_NAME + " , " + GLOBAL_LST_ID + " , " +
                                    CREATE_DT + " , " + STATUS + " , " + STATUS_DT + " , " + TYPE_LIST+
                        "  from " + TBL_LISTS + " where "+ LIST_ID + " = ? ", new String[]{String.valueOf(_idList)});
        if (c!= null && c.getCount() > 0){
          c.moveToFirst();
          do {
            lst.listId       = c.getInt   (c.getColumnIndex(LIST_ID      ));
            lst.listName     = c.getString(c.getColumnIndex(LIST_NAME    ));
            lst.globalListId = c.getInt   (c.getColumnIndex(GLOBAL_LST_ID));
            lst.createDate   = c.getString(c.getColumnIndex(CREATE_DT    ));
            lst.status       = c.getString(c.getColumnIndex(STATUS       ));
            lst.statusDate   = c.getString(c.getColumnIndex(STATUS_DT    ));
            lst.typeList     = c.getString(c.getColumnIndex(TYPE_LIST    ));
          } while (c.moveToNext());
        }
      }
      finally{c.close();/* db.close();*/}
      return lst;
  }
  //-----------------------------------------------------------------
  public int copyList(int _listIdOrig){
    int listId = 0;
    BList lst = getListById(_listIdOrig);
    try {
      listId = addList(lst);
    }catch (Exception e){e.printStackTrace();}
    return listId;
  }
  //-----------------------------------------------------------------
  public int addList(BList _data) throws Exception{
	ContentValues cv = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    int listId = getMaxListId()+1;
    if(_data != null){
//      try{
        cv = new ContentValues();
        cv.put(LIST_ID  , listId        );
        cv.put(LIST_NAME, _data.listName);
        cv.put(STATUS   , "C"           );
        cv.put(CREATE_DT, dateFormat.format(date));
        cv.put(STATUS_DT, dateFormat.format(date));
        cv.put(TYPE_LIST, "C");
        db.insert(TBL_LISTS, null, cv );
        Log.d("Test", " DB insert: " + db.isOpen());
//      }
//      finally{db.close();}
//      String sql = "INSERT INTO "+TBL_LISTS+" (LIST_ID, LIST_NAME, STATUS, CREATE_DT, STATUS_DT, TYPE_LIST) " +
//      		       " VALUES("+(++listId)+",'"+_data.listName+"','C','"+dateFormat.format(date)+"','"+dateFormat.format(date)+"','C')" ;
//      db.execSQL(sql);
    }
    return listId;
  }
  //-----------------------------------------------------------------
  public String checkStatusList(int _listId){
	int cntNotBay = 0;
	String status = null;
	Cursor c = db.rawQuery("SELECT count(" + PROD_ID + ") as _id from " + TBL_PRODLST                    +
	                       "  where LIST_ID = ? and PROD_IS_BAY!='Y' " , new String[]{String.valueOf(_listId)});
	if (c!= null && c.getCount() > 0){
	  c.moveToFirst();
	  do {
	    cntNotBay = c.getInt(c.getColumnIndex(_ID));
	  } while (c.moveToNext());
	  if(cntNotBay==0){
	    ContentValues cv = new ContentValues();
	    cv.put(STATUS, "X");
	    db.update(TBL_LISTS, cv, LIST_ID+" = ?", new String[]{String.valueOf(_listId)});
	    status = "X";
	  }else{
	    ContentValues cv = new ContentValues();
		cv.put(STATUS, "C");
		db.update(TBL_LISTS, cv, LIST_ID+" = ?", new String[]{String.valueOf(_listId)});
		status = "C";
	  }
	}
	return status;
  }
  //------------------------------------------------------------
  public void editListName(BList _list){
	if(_list != null){
//      try{
	    ContentValues cv = new ContentValues();
	    cv.put(LIST_NAME, _list.listName);
	    db.update(TBL_LISTS, cv, LIST_ID +" = ? ", new String[]{String.valueOf(_list.listId)});
	    
//      }
//      finally{ db.close();}
	}
  }
  //-----------------------------------------------------------------
  public void deleteAllProductsFromList(int _listId){
	db.delete(TBL_PRODLST, LIST_ID + "=" + _listId, null);
  }
  //-----------------------------------------------------------------
  public void deleteList(Integer _listId){
	db.delete(TBL_LISTS, LIST_ID + "=" + _listId, null);
	deleteAllProductsFromList(_listId);
//	db.close();
  }
  //-----------------------------------------------------------------
  public void beginTransaction(){
    if(db!= null && db.isOpen()) db.beginTransaction();
  }
  //-----------------------------------------------------------------
  public void endTransaction(){
    if(db!= null && db.isOpen()) db.endTransaction();
  }
  //-----------------------------------------------------------------
//  public void closeN(){
//    if(db!= null && db.isOpen())db.close();
//  }
//  //-----------------------------------------------------------------  
//  public int getCntNumb(String _cntName) throws Exception{
//	ContentValues cv      = null;
//	Cursor        c       = null;
//	Integer       cntNumb = 0   ;
//	try{
//	  if(_cntName != null){
//	    c = db.rawQuery("SELECT "+ CNT_NUMB_ID +" as _id "+
//                        "  from " + TBL_CNT_NUMB + " where "+CNTP_NUMB_NAME+" = ? ", new String[]{_cntName});
//	    if(c!= null && c.getCount() > 0){
//	      c.moveToFirst();
//	      do {
//	        cntNumb = c.getInt(c.getColumnIndex(_ID));
//	      } while (c.moveToNext());
//	      c.close();
//	      if(cntNumb != 0){
//		    cv = new ContentValues();
//		    cv.put(CNT_NUMB_ID, cntNumb++);
//		    db.update(TBL_CNT_NUMB, cv, CNTP_NUMB_NAME+" = ?", new String[] { _cntName });
//	      }
//	      else{
//		    cv = new ContentValues();
//		    cv.put(CNTP_NUMB_NAME, _cntName);
//		    db.insert(TBL_CNT_NUMB, null, cv);
//	      }
//	    }else{
//		  cv = new ContentValues();
//		  cv.put(CNTP_NUMB_NAME, _cntName);
//		  db.insert(TBL_CNT_NUMB, null, cv);
//	    }
//	  }
//	}
//	finally{
//      if(c != null) c.close();}
//	if(cntNumb == null) throw new Exception("cntNumb is Empty");
//	return cntNumb;
//}
////-------------------------------------------------------------------------
//public void close(){
//	db.close();
//}
}
