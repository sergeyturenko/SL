package com.major.shop.common;

import android.annotation.SuppressLint;

/**
 * Created by Sergey on 05.01.14.
 */
public class SM {
  //--------------------------------------------------------------
  public static String trim(String _str){
    return _str == null ? "": _str.trim();
  }
  //--------------------------------------------------------------
  public static String trimToNull(String _str){
    return _str!=null ? (SM.isEmpty(_str)? null: trim(_str)) : null;
  }
  //--------------------------------------------------------------
  public static String trimToStrQuotesNull(String _str){
    return (_str!=null ? ((SM.isEmpty(_str)? "null" : ("'"+trim(_str))+"'")) : "null");
  }
  //--------------------------------------------------------------
  @SuppressLint("NewApi")
  public static boolean isEmpty(String _str){
    return _str==null ? true : trim(_str).isEmpty();
  }
  //--------------------------------------------------------------
  public static String trimIntToStr(Integer _int){
	return ((_int != null)? String.valueOf(_int) : "");
  } 
  //--------------------------------------------------------------
  public static String trimIntNullToStr(Integer _int){
	return ((_int != null)? String.valueOf(_int) : "null");
  } 
  //--------------------------------------------------------------
}
