package com.major.shop.createlistmodule.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import com.bugsense.trace.BugSenseHandler;
import com.google.analytics.tracking.android.EasyTracker;
import com.major.shop.R;
import com.major.shop.common.SM;
import com.major.shop.common.SwipeDismissListViewTouchListener;
import com.major.shop.createlistmodule.dialog.GetListName;
import com.major.shop.logic.ListOperLogic;
import com.major.shop.logic.ProductOperationLogic;
import com.major.shop.model.BList;
import com.major.shop.model.BProduct;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sergey on 13.01.14.
 */
@SuppressLint("NewApi")
public class ListActivityC extends ActionBarActivity implements  OnEditorActionListener{
  //===============================================================================
  public static final String    PRF_SELECTED_LST = "selected_list";
  private AutoCompleteTextView  txt_prodName   ;
  private EditText              txt_coment     ;
  private DrawerLayout          mDrawerLayout  ;
  public  ListView              mDrawerList    ;
  public  ListView              prodListView   ; 
  private SharedPreferences.Editor ed          ;
  private SharedPreferences     prf            ;
  private CharSequence          mTitle         ;
  private ActionBarDrawerToggle mDrawerToggle  ;
  private ArrayList <BList>     list           = new ArrayList<BList>();
  public  LinkedList<BProduct>  productsList   = new LinkedList<BProduct>();
  public  ListCustomAdapter     ca             = null;
  public  ProductCustomAdapter  pca            = null;
  private GetListName     lstDialog      = null;
  private Button                bt_AddNewList  = null;
  private Button                bt_AddNewProd  = null;
  private Button                bt_addOneProd  = null;
  private RelativeLayout        loAddProd      = null;
  private RelativeLayout        loPrd_list     = null;
  private int                   listId         = -1;
  protected ListActivityC       THIS           = null;
  public    Animation           anim_out       = null;
  public    Animation           anim_in        = null;
  public InputMethodManager     imm            = null;
  private    String             txtComplete[]  = null; 
  private ArrayAdapter<String>  txtCmltadapter = null;
  //===============================================================================
  //-------------------------------------------------------------------------------
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    THIS = this;
    mTitle = "";
    BugSenseHandler.initAndStartSession(ListActivityC.this, "5a0e4137");
    prf = PreferenceManager.getDefaultSharedPreferences(this);
    listId = getlistIdInPref();
    ed = prf.edit();
    bt_AddNewList = (Button              )findViewById(R.id.bt_add          );
    bt_addOneProd = (Button              )findViewById(R.id.btn_prod_add    );
    mDrawerLayout = (DrawerLayout        )findViewById(R.id.drawer_layout   );
    bt_AddNewProd = (Button              )findViewById(R.id.bt_add_list     );
    loAddProd     = (RelativeLayout      )findViewById(R.id.add_prod        );
	txt_prodName  = (AutoCompleteTextView)findViewById(R.id.txt_productName1);
	txt_coment    = (EditText            )findViewById(R.id.txt_comment     );
    mDrawerList   = (ListView            )findViewById(R.id.left_drawer     );
    prodListView  = (ListView            )findViewById(R.id.lvMain          );
    loPrd_list    = (RelativeLayout      )findViewById(R.id.prd_list_layout );
    
    txt_prodName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);   
    registerForContextMenu(mDrawerList);
    ca  = new ListCustomAdapter   (this, list, "G"   );
    pca = new ProductCustomAdapter(this, productsList);
    txtComplete = new ProductOperationLogic(this).getProductsGls();
    txtCmltadapter = new ArrayAdapter<String>(this, R.layout.item_txt_complete, txtComplete);
    txt_prodName.setThreshold(1);

	txt_prodName.setAdapter(txtCmltadapter);
	txt_prodName.setDropDownHeight(180);
    mDrawerList .setAdapter(ca );
    prodListView.setAdapter(pca);
    loadAllLists();
   
    imm = (InputMethodManager)THIS.getSystemService(Context.INPUT_METHOD_SERVICE);
    
    prodListView.buildDrawingCache(true);
    
    if(listId>=0){
      ca.setSelectedItem(getPositionInList(listId));  
      productsList = new ProductOperationLogic(THIS).getProductsList(listId);
	  pca.setList(productsList);
	  pca.notifyDataSetChanged();
    }else 
      mDrawerLayout.openDrawer(GravityCompat.START);

    ActionBar bar = getActionBar();
    bar.setBackgroundDrawable(new ColorDrawable(R.color.actBar));
    
    anim_out = AnimationUtils.loadAnimation(this, R.anim.slide_out);
    anim_in  = AnimationUtils.loadAnimation(this, R.anim.slide_in );

    mDrawerList .setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BList lst = (BList) mDrawerList.getItemAtPosition(position);
        productsList = new ProductOperationLogic(THIS).getProductsList(lst.listId);
        listId = lst.listId;
        ca.setSelectedItem(position);

        pca.setList(productsList);
        mDrawerLayout.closeDrawers();
        pca.notifyDataSetChanged();
      }
    });
    
    mDrawerList.setOnItemLongClickListener(new OnItemLongClickListener() {
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        BList lst = ca.getList(position);
        if (lst != null) {
          new ListNameDialog(THIS).show(imm, lst.listId, lst.listName);
        }
        return false;
      }
    });
    
    prodListView .setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawers();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        imm.hideSoftInputFromWindow(THIS.getWindow().getDecorView().getWindowToken(), 0);
        if (loAddProd.isShown()) {
          loAddProd.setVisibility(View.GONE);
          bt_AddNewProd.setText(R.string.addProduct);
          txt_prodName.setText("");
        }
      }
    });
    
    loPrd_list.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
    	mDrawerLayout.closeDrawers();	
  		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
  		imm.hideSoftInputFromWindow(THIS.getWindow().getDecorView().getWindowToken(), 0);
      	if(loAddProd.isShown()){
          loAddProd.setVisibility(View.GONE);
          bt_AddNewProd.setText(R.string.addProduct);
          txt_prodName.setText("");
        }
	  }
    });
    
    bt_addOneProd.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        ProductOperationLogic pol = new ProductOperationLogic(THIS);
        BProduct data = getDataFromView();
        if(listId < 0) Toast.makeText(THIS, R.string.listNoCreate, Toast.LENGTH_SHORT).show();
        if(isValidateData(data)){
          try { pol.addProductInList(listId, data); } catch (Exception e) { e.printStackTrace(); }
          reloadAndSetProdList(listId);
          pca.notifyDataSetChanged();
          bt_AddNewProd.performClick();
          imm.hideSoftInputFromWindow(THIS.getWindow().getDecorView().getWindowToken(), 0);
          txtCmltadapter.add(data.productName);
          txtCmltadapter.notifyDataSetChanged();
          clearFields();
          loadAllLists();
        }
      }
    });
    
    bt_AddNewProd.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
    	if(loAddProd.isShown()){
    	  bt_AddNewProd.setText(R.string.addProduct);
    	  loAddProd.setVisibility(View.GONE);
    	  imm.hideSoftInputFromWindow(THIS.getWindow().getDecorView().getWindowToken(), 0);
    	  txt_prodName.setText("");
    	}
    	else{
    	  bt_AddNewProd.setText(R.string.cancelProduct);
    	  loAddProd.setVisibility(View.VISIBLE);
    	  txt_prodName.requestFocus();
    	  imm.showSoftInput(txt_prodName, InputMethodManager.SHOW_IMPLICIT);
    	}
	  }
    });
    
    bt_AddNewList.setOnClickListener(new OnClickListener() {
	  public void onClick(View v) {
		lstDialog = new GetListName(THIS);
		lstDialog.createDefaultNameList();
		loadAllLists();
		ca.setSelectedItem(getPositionInList(list.get(0).listId)); 
		mDrawerLayout.closeDrawers();
		mDrawerList.performItemClick(
				mDrawerList.getAdapter().getView(0, null, null),
		        0, mDrawerList.getAdapter().getItemId(0));
		bt_AddNewProd.setText(R.string.cancelProduct);
   	    loAddProd.setVisibility(View.VISIBLE);
   	    txt_prodName.requestFocus();
   	    imm.showSoftInput(txt_prodName, InputMethodManager.SHOW_IMPLICIT);
	  } 
	});
      	
    mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_navigation_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
    ) {
 
      public void onDrawerClosed(View view) { getActionBar().setTitle(R.string.app_name); }

      public void onDrawerOpened(View drawerView) { getActionBar().setTitle(R.string.ab_list); }
      
    };

    mDrawerLayout.setDrawerListener(mDrawerToggle);
 
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled     (true);

    //===============================================================================================
    SwipeDismissListViewTouchListener touchListener =
            new SwipeDismissListViewTouchListener(
            		prodListView,
                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
            	      public boolean canDismiss(int position) { return true; }
                      public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        BProduct prd = null;
                        for (int position : reverseSortedPositions) {
                          prd = productsList.get(position);
                          productsList.remove(position);
                          new ProductOperationLogic(THIS).deleteProductFromList(listId, prd.productId);
                          new ListOperLogic(THIS).checkStatusList(listId);
                          loadAllLists();
                        }
                        pca.notifyDataSetChanged();
                      }
             });
    

    prodListView.setOnTouchListener(touchListener);

    prodListView.setOnScrollListener(touchListener.makeScrollListener());
        
    //===============================================================================================
    
//    SwipeDismissListViewTouchListener touchListener2 =
//            new SwipeDismissListViewTouchListener(
//            		mDrawerList,
//                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
//                        @Override
//                        public boolean canDismiss(int position) {
//                            return true;
//                        }
//
//                        @Override
//                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
//                        	BList lst = null;
//                            for (int position : reverseSortedPositions) {
//                              lst = list.get(position);
//                              list.remove(position);
//                              new ListOperLogic(THIS).deleteList(lst.listId);
////                            	ca.remove(pca.getItem(position));
//                            }
//                            ca.notifyDataSetChanged();
//                        }
//                    });
    
    
//    mDrawerLayout.setOnTouchListener(touchListener);
    
    anim_out.setAnimationListener(new AnimationListener() {
      public void onAnimationStart (Animation animation) { }
      public void onAnimationRepeat(Animation animation) { }
      public void onAnimationEnd(Animation animation) {
    	pca.changePositionElementList();
      }
    });
  //===============================================================================================
    }
    //---------------------------------------------------
    protected int getFirstIndexProdBay(){
  	BProduct prd = null;
  	int index = 0;
  	for(int i=0; i<list.size();i++){
  	  prd = productsList.get(i);
  	  if("Y".equalsIgnoreCase(prd.isBay)){
  		index = i;
  		break;
  	  }
  	}
  	return index;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main, menu);
      return true;
    }
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      String msg = null;
      if (item.getItemId()== R.id.action_share){
        if((msg = getListStr())== null)
            Toast.makeText(THIS, R.string.listNoCreate, Toast.LENGTH_SHORT).show();
        else{
          Intent sendIntent = new Intent();
          sendIntent.setAction(Intent.ACTION_SEND);
          sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
          sendIntent.setType("text/plain");
          startActivity(sendIntent);
        }
      }
//      if (item.getItemId()== R.id.add_list_action){
//    	if(loAddProd.isShown()) loAddProd.setVisibility(View.GONE);
//        else loAddProd.setVisibility(View.VISIBLE); 
//    	return true;
//      }
      if (mDrawerToggle.onOptionsItemSelected(item)) { return true; }
      return super.onOptionsItemSelected(item);
    }
    //----------------------------------------------------
    public BProduct getDataFromView(){
  	  BProduct data = new BProduct();
  	  data.listId      = listId;
  	  data.productName = SM.trim(txt_prodName .getText().toString());
  	  data.comment     = SM.trim(txt_coment   .getText().toString());
  	  return data;
    }
    //----------------------------------------------------
    public void clearFields(){
  	  txt_prodName .setText("");
  	  txt_coment   .setText("");
    }
    //-------------------------------------------------------------------
    @Override
    protected void onResume() {
      super.onResume();
      loadAllLists();
    }
    
    //-------------------------------------------------------------------
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
   //-------------------------------------------------------------------
   public void reloadAndSetProdList(int _listId){
	 productsList = new ProductOperationLogic(THIS).getProductsList(_listId);
	 pca.setList(productsList);
	 pca.notifyDataSetChanged();
   }
   //-------------------------------------------------------------------  
   public void loadAllLists(){
  	 ListOperLogic lol = new ListOperLogic(this);
     list = lol.getAllLists("G");
     ca.setList(list);
     mDrawerList.setAdapter(ca);
     ca.notifyDataSetChanged();
   }
   //-------------------------------------------------------------------
   protected void onStart() {
 	super.onStop();
 	BugSenseHandler.startSession(ListActivityC.this);
 	EasyTracker.getInstance(this).activityStart(this);
   };
   //-------------------------------------------------------------------
   protected void onStop() {
 	super.onStop();
 	BugSenseHandler.flush(ListActivityC.this);
 	EasyTracker.getInstance(this).activityStop(this);
   };
   //-------------------------------------------------------------------
   @Override
   public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
 	 switch (actionId) {
 	   case EditorInfo.IME_ACTION_DONE: bt_addOneProd.performClick();  return true;
 	 }
     return false;
   }
   //-------------------------------------------------------------------
   public int getlistIdInPref(){
	 int lstId = 0;
	 lstId = prf.getInt("selected_list", -1);
	 return lstId;
   }
   //----------------------------------------------------
   public boolean isValidateData(BProduct _data){
 	boolean isValid = false;
 	if(_data != null) isValid = !SM.isEmpty(_data.productName);
 	if(!isValid) Toast.makeText(this, R.string.productNameIsEmpty, Toast.LENGTH_SHORT).show();
     return isValid;
   }
   //-------------------------------------------------------------------
   public void removeListIdInPref(){
	 ed.remove("selected_list");
	 ed.commit();
   }
   //-------------------------------------------------------------------
   public void savelistIdInPref(int _listId){
	 ed.putInt(PRF_SELECTED_LST, _listId);
	 ed.commit();
   }
   //-------------------------------------------------------------------
   public int getPositionInList(int _listId){
	 int position = 0;
	 if(list!= null)
	   for(int i=0; i<list.size();i++){
	     if(list.get(i).listId == _listId){
	       position = i;
	       break;
	     }
	   }
	 return position;
   }
   //-------------------------------------------------------------------
  public String getListStr(){
    boolean hasNotCheck = false;
    StringBuilder sb = new StringBuilder();
     if(productsList!= null && productsList.size()>0){
         sb.append(R.string.app_name).append(":\n");
         for(BProduct p : productsList){
             if(!"Y".equalsIgnoreCase(p.isBay)){
                 hasNotCheck = true;
                 sb.append(p.productName).append(" ").append(p.comment).append(";\n");
             }
         }
     }
    return hasNotCheck ? sb.toString() : null;
  }
   //-------------------------------------------------------------------
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
     switch(keyCode){
       case KeyEvent.KEYCODE_BACK:
    	 if(loAddProd.isShown()){
    	   loAddProd.setVisibility(View.GONE);
    	   return true;
    	 }else{
    	   if(list != null && list.size()>0)
    	     if(!"X".equalsIgnoreCase(list.get(getPositionInList(listId)).status))
    	       savelistIdInPref(listId);
    	     else
    		   removeListIdInPref();
    	   return super.onKeyDown(keyCode, event);
    	 }
     }
     return super.onKeyDown(keyCode, event);
   }
  //-------------------------------------------------------------------

}