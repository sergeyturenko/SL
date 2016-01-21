package com.major.shop.model;

import java.util.ArrayList;

/**
 * Created by Sergey on 12.01.14.
 */
public class BList {
  //==============================================
  public int    listId        = 0   ;
  public int    globalListId  = 0   ;
  public String createDate    = null;
  public String listName      = null;
  public String status        = null;
  public String statusDate    = null;
  /**(C - created/R - rescivered)*/
  public String typeList      = null;
  /**S- обычная выборка; F- полная выборка*/
  public String modeLoad      = "S";
  public String strProdFromLst= null;
  public BProducts products   = null;
  //==============================================
  //----------------------------------------------
  @Override
  public String toString() {
	  return "BList [listId=" + listId + ", globalListId=" + globalListId
	  	   + ", createDate=" + createDate + ", listName=" + listName
		   + ", status=" + status + ", statusDate=" + statusDate
		   + ", typeList=" + typeList + ", modeLoad="+modeLoad+", " 
		   + "strProdFromLst="+strProdFromLst+", porducts="+products+"]";
  }
  //----------------------------------------------
}
