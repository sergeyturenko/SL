package com.major.shop.createlistmodule.activity;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.major.shop.R;
import com.major.shop.common.SM;
import com.major.shop.logic.ListOperLogic;
import com.major.shop.logic.ProductOperationLogic;
import com.major.shop.model.BList;
import com.major.shop.model.BProduct;

public class ListNameDialog implements OnClickListener{
	  //====================================================
	  private EditText dialogEditBox  ;
	  private Button   dialogButtonYes;
      private Button   copyListButton ;
	  private Button   dialogButtonNo ;
	  private Dialog   dialog         ;
	  public final String LOG  = "LIST_ACTIVITY";       
	  private ListActivityC activity  ;
	  private int      listId     = -1;
	  public static String LIST_DEFAULT_NAME = "listDefNameNumber";
	  public InputMethodManager imm;
	  //====================================================
	  //----------------------------------------------------
	  public ListNameDialog( ListActivityC _activity ){
	    activity = _activity;
		init();
	  }
	  //----------------------------------------------------
	  private void init(){
	    dialog = new Dialog(activity);
		dialog.setTitle(R.string.Input_list_name);
	    dialog.setContentView(R.layout.get_list_name_dialog);		
	  //----------------------------------------------------
	    dialog.findViewById(R.id.dialogListName).setFocusable(true);
		dialogButtonYes = (Button) dialog.findViewById( R.id.dialogListButtonYes    );
		copyListButton  = (Button) dialog.findViewById( R.id.copyButtonList         );
		dialogButtonNo  = (Button) dialog.findViewById( R.id.dialogListButtonCancel );
		dialogButtonYes.setOnClickListener(this);
		copyListButton .setOnClickListener(this);
		dialogButtonNo .setOnClickListener(this);
	  }
	  //----------------------------------------------------
	  public int getIntId(){
		return listId;
	  }
	  //----------------------------------------------------
	  public void show(InputMethodManager _imm, int _listId, String _listName){
		dialog.show();		
		listId = _listId;
		imm    = _imm   ;
	    EditText txt_listName = (EditText)dialog.findViewById(R.id.dialogListName);
	    txt_listName.setText(_listName);
	    txt_listName.setSelection(txt_listName.getText().length());
	    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	  }
	  //----------------------------------------------------
	  @Override
	  public void onClick(View v) {
		dialogEditBox = (EditText) dialog.findViewById( R.id.dialogListName );
	    if(v.getId()==R.id.dialogListButtonYes){
	      if(dialogEditBox == null || dialogEditBox.getText()== null || SM.isEmpty(dialogEditBox.getText().toString())) 
			Toast.makeText(activity, R.string.productNameIsEmpty, Toast.LENGTH_SHORT).show();
		  else saveResultAddList(dialogEditBox.getText().toString());
	    }else if(v.getId()==R.id.copyButtonList){
			listId = copyListWithProducts(listId);
            activity.setListId(listId);
			activity.loadAllLists();
			activity.reloadAndSetProdList(listId);
			activity.ca.setSelectedItem(activity.getPositionInList(listId));
			activity.pca.notifyDataSetChanged();
			activity.ca.notifyDataSetChanged();
		}
	    imm.hideSoftInputFromWindow(dialogEditBox.getWindowToken(), 0);
	    imm      = null;
	    activity = null;
        dialog.cancel();
	  }
	  //----------------------------------------------------
	  public void saveResultAddList(String _listName){
		try{
		  ListOperLogic logic = new ListOperLogic(activity);
		  BList lst = new BList();
		  lst.listName = _listName;
		  lst.listId   = listId   ;
		  logic.editListName(lst);
		  activity.loadAllLists();
		  activity.ca.notifyDataSetChanged();
		}
		catch(Exception e){ Log.e(LOG, e.toString()); Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();}
	  }
	  //----------------------------------------------------
	public int copyListWithProducts(int _listId){
		ListOperLogic logic = new ListOperLogic(activity);
		int listId = logic.copyList(_listId);

		ProductOperationLogic prodLogic = new ProductOperationLogic(activity);
		prodLogic.copyProductsInNewList(_listId, listId);
		return listId;
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