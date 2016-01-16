package com.major.shop.common;

import android.widget.EditText;

public class VU {
  //--------------------------------------------------------
  public static void setEnableEditText(boolean _flag, EditText ... _ob){
	if(_ob != null && _ob.length > 0)
	  for(int i=0; i<_ob.length;i++){
		_ob[i].setEnabled(_flag);
	  }
  }
  //--------------------------------------------------------
  public static void setFocusibleEditText(boolean _flag, EditText ... _ob){
	if(_ob != null && _ob.length > 0)
	  for(int i=0; i<_ob.length;i++){
		if(_flag)_ob[i].setFocusable(_flag);
		else _ob[i].clearFocus();
	  }
  }
  //--------------------------------------------------------
}
