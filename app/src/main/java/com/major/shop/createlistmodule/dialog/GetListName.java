package com.major.shop.createlistmodule.dialog;

import java.util.Date;

import com.major.shop.R;
import com.major.shop.common.SM;
import com.major.shop.createlistmodule.activity.ListActivityC;
import com.major.shop.logic.ListOperLogic;
import com.major.shop.model.BList;
import com.major.shop.model.BProduct;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * mode = "C" режим копирования листа
 * */
public class GetListName implements OnClickListener{
  //====================================================
  public final String LOG  = "LIST_ACTIVITY";
  private EditText dialogEditBox  ;
  private Button   dialogButtonYes;
  private Button   copyListButton ;
  private Button   dialogButtonNo ;
  private Dialog   dialog         ;
  private String   mode           = "B";
  private Activity activity       ;
  private SharedPreferences       preferense = null;
  private int      listId     = -1;
  private int      origListId = -1;
  public static String LIST_DEFAULT_NAME = "listDefNameNumber";
  private boolean isShowAndSetProduct = false;
  //====================================================
  //----------------------------------------------------
  public GetListName( Activity _activity ){
    activity = _activity;
	init();
  }
  //----------------------------------------------------
  public GetListName( Activity _activity, String _mode, int _origListId){
    activity   = _activity  ;
    mode       = _mode      ;
    origListId = _origListId;
	init();
  }
  //----------------------------------------------------
  private void init(){
    dialog = new Dialog(activity);
	dialog.setTitle(R.string.Input_list_name);
    dialog.setContentView(R.layout.get_list_name_dialog);		
  //----------------------------------------------------
    dialog.findViewById(R.id.dialogListName).setFocusable(true);
	dialogButtonYes = (Button) dialog.findViewById( R.id.dialogListButtonYes   );
    copyListButton  = (Button) dialog.findViewById( R.id.copyButtonList        );
	dialogButtonNo  = (Button) dialog.findViewById( R.id.dialogListButtonCancel);
	dialogButtonYes.setOnClickListener(this);
    copyListButton .setOnClickListener(this);
	dialogButtonNo .setOnClickListener(this);
  }
  //----------------------------------------------------
  public int getIntId(){
	return listId;
  }
  //----------------------------------------------------
  public void createDefaultNameList(){
	String date = getDafaultName();
	SharedPreferences.Editor ed = null;
	preferense = activity.getPreferences(activity.MODE_PRIVATE);
	int number = 0;
	ed = preferense.edit();
	if(preferense.contains(LIST_DEFAULT_NAME)){
	  number = preferense.getInt(LIST_DEFAULT_NAME, 0);
	}
	    
	ed.putInt(LIST_DEFAULT_NAME, ++number);
	ed.commit();
	saveResultAddList(date);
  }
  //----------------------------------------------------
  public String getDafaultName(){
	return DateFormat.format("dd.MM.yy kk:mm", new java.util.Date()).toString();
  }
  //----------------------------------------------------
  public void show(){
	dialog.show();		
	String date = DateFormat.format("dd.MM.yy kk:mm", new java.util.Date()).toString();
    SharedPreferences.Editor ed = null;
    preferense = activity.getPreferences(activity.MODE_PRIVATE);
    int number = 0;
    ed = preferense.edit();
    if(preferense.contains(LIST_DEFAULT_NAME)){
      number = preferense.getInt(LIST_DEFAULT_NAME, 0);
    }
    
    ed.putInt(LIST_DEFAULT_NAME, ++number);
    ed.commit();
    EditText txt_listName = (EditText)dialog.findViewById(R.id.dialogListName);
    txt_listName.setText(date);
  }
  //----------------------------------------------------
  public void showAndSetProduct(){
	dialog.show();
	isShowAndSetProduct = true;
	String date = android.text.format.DateFormat.format("dd.MM", new java.util.Date()).toString();
    SharedPreferences.Editor ed = null;
    preferense = activity.getPreferences(activity.MODE_PRIVATE);
    int number = 0;
    ed = preferense.edit();
    if(preferense.contains(LIST_DEFAULT_NAME)){
      number = preferense.getInt(LIST_DEFAULT_NAME, 0);
    }

    SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(activity);
    String listName = prf.getString("ListNamePrefix", "");
    
    ed.putInt(LIST_DEFAULT_NAME, ++number);
    ed.commit();
    EditText txt_listName = (EditText)dialog.findViewById(R.id.dialogListName);
    txt_listName.setText(listName+"_"+date);
  }
  //----------------------------------------------------
  @Override
  public void onClick(View v) {
	dialogEditBox = (EditText) dialog.findViewById( R.id.dialogListName );
    if(v.getId()==R.id.dialogListButtonYes){
      if(dialogEditBox == null || dialogEditBox.getText()== null || SM.isEmpty(dialogEditBox.getText().toString())) 
		Toast.makeText(activity, R.string.productNameIsEmpty, Toast.LENGTH_SHORT).show();
	  else saveResultAddList(dialogEditBox.getText().toString());
	  dialog.cancel();
	  if(ListActivityC.class.isInstance(activity)){
	    ((ListActivityC)activity).loadAllLists();
//	    ((ListActivityC)activity).rePaint();
	  }
    }
    else if(v.getId() == R.id.dialogListButtonCancel) dialog.cancel();
    else if(v.getId() == R.id.copyButtonList){
      dialog.cancel();
    }
  }
  //----------------------------------------------------
  public void saveResultAddList(String _listName){
	try{
	  ListOperLogic logic = new ListOperLogic(activity);
	  BList lst = new BList();
	  lst.listName = _listName;
	  listId = logic.addList(lst);
	  if("C".equalsIgnoreCase(mode) && origListId>=0){
//		new ListOperLogic(activity).copyProductsInNewList(origListId, listId);
	  }
	  if(ListActivityC.class.isInstance(activity)){
	    int listId = 0;
	    listId = getIntId();
	    if(listId>=0){
//	      Intent intent = new Intent(activity, ProductActivity.class);
//	      intent.putExtra("listId", listId);
//	      if(isShowAndSetProduct)
//	    	intent.putExtra("mode", 'A');
//	      else
//		    intent.putExtra("mode", 'E');
//		  activity.startActivity(intent);
	    }
	  }
	}
	catch(Exception e){ Log.e(LOG, e.toString()); Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();}
  }
  //----------------------------------------------------
  public boolean isValidateData(BProduct _data){
	boolean isValid = false;
	if (_data != null) isValid = !SM.isEmpty(_data.productName);
	if(!isValid) Toast.makeText(activity, R.string.productNameIsEmpty, Toast.LENGTH_SHORT).show();
    return isValid;
  }
  //----------------------------------------------------
}
