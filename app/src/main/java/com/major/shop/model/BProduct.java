package com.major.shop.model;

/**
 * Created by Sergey on 23.12.13.
 */
public class BProduct {
  //==============================================================
  public  int     listId      = 0 ;
  public  int     productId   = -1;
  public  String  productName = "";
  public  int     unitId      = 0 ;
  public  String  unitName    = "";
  public  String  productCnt  = "";
  public  String  comment     = "";
  public  String  isBay       = "N";
  //==============================================================
  //--------------------------------------------------------------
  @Override
  public String toString() {
	return "BProduct [listId=" + listId + ", productId=" + productId
			+ ", productName=" + productName + ", unitId=" + unitId
			+ ", unitName=" + unitName + ", productCnt=" + productCnt
			+ ", comment=" + comment + ", isBay=" + isBay + "]";
}
  
  //--------------------------------------------------------------
}
