package com.major.shop.createlistmodule.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.major.shop.R;
import com.major.shop.common.SM;
import com.major.shop.createlistmodule.activity.ListActivityC;

import com.major.shop.model.BList;
import com.major.shop.model.BProduct;

public class ActionListDialog implements OnClickListener{
  //====================================================
  private EditText dialogEditBox     ;
  private Button   dialogButtonEdit  ;
  private Button   dialogButtonDel   ;
  private Button   dialogButtonCancel;
  private Dialog   dialog            ;
  private ListActivityC activity     ;
  private BList    listItem    = null;
  private SharedPreferences       preferense = null;
  public static String LIST_DEFAULT_NAME = "listDefNameNumber";
  //====================================================
  //----------------------------------------------------
  public ActionListDialog( Activity _activity ){
    activity = (ListActivityC)_activity;
	init();
  }
  //----------------------------------------------------
  private void init(){
    dialog = new Dialog(activity);
	dialog.setTitle(R.string.actionL);	
//	dialog.setContentView(R.layout.action_list_dialog);
  //----------------------------------------------------
//	dialogButtonEdit   = (Button) dialog.findViewById( R.id.editL      );
//	dialogButtonDel    = (Button) dialog.findViewById( R.id.deleteListL);
//	dialogButtonCancel = (Button) dialog.findViewById( R.id.cancelL    );
//	dialogButtonEdit   .setOnClickListener(this);
//	dialogButtonDel    .setOnClickListener(this);
//	dialogButtonCancel .setOnClickListener(this);
  }
//  //----------------------------------------------------	
//  public void show(BList _listItem){
//	listItem = _listItem;
//    dialog.show();
//  }
//  //----------------------------------------------------
//  @Override
//  public void onClick(View v) {
//	if(v.getId()==R.id.editL){
//      Intent intent = new Intent(activity, ProductActivity.class);
//      intent.putExtra("listId", listItem.listId);
//      intent.putExtra("mode"  , 'E');
//      activity.startActivity(intent);
//      dialog.cancel();
//	}
//	else if(v.getId()==R.id.deleteListL){
//	  deleteList(listItem.listId);
//	  activity.loadAllLists();
//	  dialog.cancel();
//	}
//    else if(v.getId()==R.id.cancelL)dialog.cancel();  
//
//  }
//  //----------------------------------------------------
//  public void deleteList (int _listId){
//    ListOperLogic logic = new ListOperLogic(activity);
//    logic.deleteList(_listId);
//  }
//  //----------------------------------------------------
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
}
}
