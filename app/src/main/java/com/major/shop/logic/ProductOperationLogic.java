package com.major.shop.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.major.shop.R;
import com.major.shop.common.SM;
import com.major.shop.model.BProduct;
import com.major.shop.model.BProducts;
import com.major.shop.model.BUnit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Sergey on 12.01.14.
 */
public class ProductOperationLogic extends ListOperLogic{
  //-----------------------------------------------------------------
  public ProductOperationLogic(Context _context) {
    super(_context);
  }
  //-----------------------------------------------------------------
  public LinkedList<BProduct> getProductsList(int _idList){
    LinkedList<BProduct> products = new LinkedList<BProduct>();
    BProduct  prd = null;
    Cursor c = null;
    try{
      if (db.isOpen()){
      c = db.rawQuery("SELECT "+ PROD_ID +" as _id, "+LIST_ID+", "+PROD_NAME+", "+PROD_CNT+", "+
              UNIT_NAME+", "+COMMENT+ ", "+PROD_IS_BAY+
              "  from " + TBL_PRODLST +" where LIST_ID = ? order by "+DT_PRODUCT+", "+DT_BAY_PRODUCT+" desc ", new String[]{String.valueOf(_idList)});
      if (c!= null && c.getCount() > 0) {
        c.moveToFirst();
        do {
          prd = new BProduct();
          prd.productId = c.getInt(c.getColumnIndex(_ID));
          prd.listId = c.getInt(c.getColumnIndex(LIST_ID));
          prd.productName = c.getString(c.getColumnIndex(PROD_NAME));
          prd.productCnt = c.getString(c.getColumnIndex(PROD_CNT));
          prd.unitName = c.getString(c.getColumnIndex(UNIT_NAME));
          prd.comment = c.getString(c.getColumnIndex(COMMENT));
          prd.isBay = c.getString(c.getColumnIndex(PROD_IS_BAY));
          products.add(prd);
        } while (c.moveToNext());
      }
      }
    }finally{if(c!= null)c.close();}
    return products;
  }
  //-----------------------------------------------------------------
  public BProducts getProductsLst(int _idList){
	BProducts products = new BProducts();
	BProduct  prd = null;
    Cursor c = null;
//    try{
      c = db.rawQuery("SELECT "+ PROD_ID +" as _id, "+LIST_ID+", "+PROD_NAME+", "+PROD_CNT+", "+
    	                        UNIT_NAME+", "+COMMENT+ ", "+PROD_IS_BAY+
                              "  from " + TBL_PRODLST +" where LIST_ID = ? order by "+DT_PRODUCT+", "+DT_BAY_PRODUCT+" desc ", new String[]{String.valueOf(_idList)});
      if (c!= null && c.getCount() > 0){
        c.moveToFirst();
        do {
          prd = new BProduct();
    	  prd.productId   = c.getInt   (c.getColumnIndex(_ID      ));
    	  prd.listId      = c.getInt   (c.getColumnIndex(LIST_ID  ));
          prd.productName = c.getString(c.getColumnIndex(PROD_NAME));
          prd.productCnt  = c.getString(c.getColumnIndex(PROD_CNT ));
          prd.unitName    = c.getString(c.getColumnIndex(UNIT_NAME));
          prd.comment     = c.getString(c.getColumnIndex(COMMENT  ));
          prd.isBay       = c.getString(c.getColumnIndex(PROD_IS_BAY));
          products.products.add(prd);
        } while (c.moveToNext());
      }
//    }finally{c .close();
//             db.close();}
    return products;
  }

  //-----------------------------------------------------------------
  public BProduct getProduct(int _idList, int _prodId){
    BProduct product = new BProduct();
    Cursor   c       = null;
//    try{
       c = db.rawQuery("SELECT "+ LIST_ID +" as _id, "+PROD_NAME+", "+PROD_CNT+", "+
    	  	                  UNIT_ID+", "+COMMENT+ ", "+PROD_IS_BAY+
                             "  from " + TBL_PRODLST + " where LIST_ID = ? " +
                             "   and  "+ PROD_ID     + " = ? ",
                              new String[]{String.valueOf(_idList), String.valueOf(_prodId)});
      if (c!= null && c.getCount() > 0){
        c.moveToFirst();
        do {
          product.listId     = c.getInt   (c.getColumnIndex(_ID        ));
          product.productId  = c.getInt   (c.getColumnIndex(PROD_ID));
          product.productName= c.getString(c.getColumnIndex(PROD_NAME  ));
          product.unitId     = c.getInt   (c.getColumnIndex(UNIT_ID));
          product.unitName   = c.getString(c.getColumnIndex(UNIT_NAME  ));
          product.comment    = c.getString(c.getColumnIndex(COMMENT    ));
          product.isBay      = c.getString(c.getColumnIndex(PROD_IS_BAY));
        } while (c.moveToNext());
      }
//    }
//    finally{c.close(); db.close();}
    return product;
  }
  //-----------------------------------------------------------------
  public String [] getProductsGls(){
    String str [] = null;
    Cursor c      = null; 
    int i = 0;
    try{
	  c = db.rawQuery("SELECT "+ PROD_ID +" as _id, "+PROD_NAME+
			          "  from " + TBL_PROD, null);
	  if (c!= null && c.getCount() > 0){
        str = new String[c.getCount()];
	    c.moveToFirst();
	    do {
	      str[i++] = c.getString(c.getColumnIndex(PROD_NAME));
	    } while (c.moveToNext());
	  }else str = new String[]{""};
    } finally{c.close();}
//    finally{c.close(); db.close();}
	return str;
  }
  //-----------------------------------------------------------------
//  public String [] getUnitsGls(){
//    String str [] = null;
//    Cursor c      = null; 
//    int i = 0;
//    try{
//	  c = db.rawQuery("SELECT "+ UNIT_ID +" as _id, "+UNIT_NAME+
//			          "  from " + TBL_UNIT, null);
//	  if (c!= null && c.getCount() > 0){
//        str = new String[c.getCount()];
//	    c.moveToFirst();
//	    do {
//	      str[i++] = c.getString(c.getColumnIndex(UNIT_NAME));
//	    } while (c.moveToNext());
//	  }else str = new String[]{""};
//    }
//    finally{c.close(); db.close();}
//	return str;
//  }
  //-----------------------------------------------------------------
  public String addProductInList(int _listId, BProduct _data) throws Exception{
	SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
	String statusLst = null;
	addProdGloss(_data);
	ContentValues cv = new ContentValues();
    if(_data != null && _data.productId >=0){
      cv.put(LIST_ID    , _listId          );
      cv.put(PROD_ID    , _data.productId  );
      cv.put(PROD_NAME  , _data.productName);
      cv.put(PROD_CNT   , _data.productCnt );
      cv.put(UNIT_NAME  , _data.unitName   );
      cv.put(COMMENT    , _data.comment    );
      cv.put(PROD_IS_BAY, _data.isBay      );
      cv.put(DT_PRODUCT , parser.format(new Date()));
      db.insert(TBL_PRODLST, null, cv    );
      statusLst = new ListOperLogic(context).checkStatusList(_listId);
//      db.close();
    }
    return statusLst;
  }
  //-----------------------------------------------------------------
  public void reSaveProductsList(BProducts _data){
    BProduct prod = null;
	if(_data != null && _data.products!= null && _data.products.size() > 0){
      ContentValues cv = null;
//	  try{
		for(int i=0; i<_data.products.size();i++){
		  prod = _data.products.get(i);
	      cv = new ContentValues();
	      cv.put(PROD_IS_BAY, prod.isBay);
		  db.update(TBL_PRODLST, cv, LIST_ID+" = ? and "+PROD_ID+" = ? ", new String[]{String.valueOf(prod.listId), String.valueOf(prod.productId)});
		}
//      }
//	  finally{ db.close();}
    }
  }
  //-----------------------------------------------------------------
  public int getMaxProdId(){
	Cursor c = null;
    int maxId = 0;
    try{
      c = db.rawQuery("SELECT max(" + PROD_ID + ") as _id " + 
                      "  from " + TBL_PROD , null);
      if (c!= null && c.getCount() > 0){
        c.moveToFirst();
        do {
          maxId = c.getInt(c.getColumnIndex(_ID));
        } while (c.moveToNext());
      }
    }finally{if(c!= null)c.close();}
    return maxId;
  }
  //-----------------------------------------------------------------
  public int getMaxProdListId(){
	Cursor c = null;
    int maxId = 0;
    try{
      c = db.rawQuery("SELECT max(" + PROD_ID + ") as _id " +
                      "  from " + TBL_PRODLST , null);
      if (c!= null && c.getCount() > 0){
        c.moveToFirst();
        do {
          maxId = c.getInt(c.getColumnIndex(_ID));
        } while (c.moveToNext());
      }
    }finally{
      if(c != null && !c.isClosed())c.close();
    }
    return maxId;
  }
  //-----------------------------------------------------------------
  public void copyProductsInNewList(int _listIdOrig, int listId){
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
    System.out.println(_listIdOrig+" - "+listId+ " - "+db);
//    try{
      BProducts lst = getProductsLst(_listIdOrig);
    System.out.println("-----"+lst);
      int mid = getMaxProdListId()+1;
      if(lst!= null && lst.products != null && lst.products.size() > 0) {
        ContentValues cv = null;
        for (BProduct p : lst.products) {
          cv = new ContentValues();
          if (p != null) {
            cv.put(LIST_ID  , listId       );
            cv.put(PROD_ID  , mid++        );
            cv.put(PROD_NAME, p.productName);
            cv.put(PROD_CNT , p.productCnt );
            cv.put(UNIT_NAME, p.unitName);
            cv.put(COMMENT  , p.comment);
            cv.put(PROD_IS_BAY, "N");
            cv.put(DT_PRODUCT, parser.format(new Date()));
            db.insert(TBL_PRODLST, null, cv);
          }
        }
      }
//    }finally{db.close();}
  }
  //------------------------------------------------------------
  public void addProdGloss(BProduct _data){
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
	getProdIdForProdName(_data);
	if(_data.productId<0){
	  ContentValues cv = new ContentValues();
	  if(_data != null){
		_data.productId = getMaxProdId() + 1;
	    cv.put   (PROD_ID  , _data.productId  );
	    cv.put   (PROD_NAME, _data.productName); 
	    cv.put   (IS_ACTIVE, "Y"              );
	    cv.put   (CH_DATE  , dateFormat.format(date));
	    db.insert(TBL_PROD , null, cv);
	  }   
	}
  }
  //------------------------------------------------------------
//  public void addUnitGloss(BUnit _unit){
//	getUnitIdForUnitName(_unit);
//	if(_unit.unit_id < 0){
//	  ContentValues cv = new ContentValues();
//	  if(_unit != null){
//	    cv.put   (UNIT_NAME, _unit.unit_name  ); 
//	    cv.put   (LANGUAGE , "RU"             );
//	    db.insert(TBL_UNIT , null, cv);
//	  }   
//	}
//  }
  //------------------------------------------------------------
  public void getUnitIdForUnitName(BUnit _unit){
    Cursor c = null;
	try{
      c = db.rawQuery("SELECT "+ UNIT_ID +" as _id" +
	                  "  from " + TBL_UNIT + " where "+UNIT_NAME+" = ? ", new String[]{ String.valueOf(_unit.unit_name)});
	  if(c!= null && c.getCount() > 0){
	    c.moveToFirst();
	    do {
	      _unit.unit_id = c.getInt(c.getColumnIndex(_ID));
	    } while (c.moveToNext());
	  }   
	}
	finally{c.close();}
  }
  //------------------------------------------------------------
  public void getProdIdForProdName(BProduct _data){
	Cursor c = null;
	try{
	  c = db.rawQuery("SELECT "+ PROD_ID +" as _id" +
                           "  from " + TBL_PROD + " where "+PROD_NAME+" = ? ", new String[]{ String.valueOf(_data.productName).trim()});
      if(c!= null && c.getCount() > 0){
        c.moveToFirst();
        do {
          _data.productId = c.getInt(c.getColumnIndex(_ID));
        } while (c.moveToNext());
      }   
    }
	finally{c.close();}
  }

  //------------------------------------------------------------
  public void refreshStatusProductsGls(){
	
  }
  //------------------------------------------------------------
  public String saveStatusProduct(int _listId, BProduct _data){
	String statusLst = null;
	if(_data != null){
//	  try{
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        ContentValues cv = new ContentValues();
		cv.put(PROD_IS_BAY   , _data.isBay);
		if("Y".equalsIgnoreCase(_data.isBay))
		  cv.put(DT_BAY_PRODUCT, parser.format(new Date()));
		else
		  cv.putNull(DT_BAY_PRODUCT);
		db.update(TBL_PRODLST, cv, LIST_ID+" = ? and "+ PROD_ID+" = ?", new String[]{String.valueOf(_listId), String.valueOf(_data.productId)});
		statusLst = new ListOperLogic(context).checkStatusList(_listId);
//	  }
//	  finally{ db.close();}
    }
	return statusLst;
  }
  //------------------------------------------------------------
  public void editProductInList(BProduct _data){
	if(_data != null){
//      try{
	    addProdGloss(_data);
	    ContentValues cv = new ContentValues();
	    cv.put(PROD_NAME, _data.productName  );
	    cv.put(PROD_CNT , _data.productCnt   );
	    cv.put(UNIT_NAME, _data.unitName     );
	    cv.put(COMMENT  , _data.comment      );
	    db.update(TBL_PRODLST, cv, LIST_ID +" = ? and "+PROD_ID+" = ?", new String[]{String.valueOf(_data.listId), String.valueOf(_data.productId)});
//      }
//      finally{ db.close();}
	}
  }
  //------------------------------------------------------------
  public void deleteProductFromList(Integer _listId, Integer _prodId) {
    db.delete(TBL_PRODLST, LIST_ID + " = ? and " + PROD_ID + "= ?", new String[]{String.valueOf(_listId), String.valueOf(_prodId)});
//	db.close();
  }
  //------------------------------------------------------------
}
