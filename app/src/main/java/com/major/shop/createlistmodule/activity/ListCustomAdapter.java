package com.major.shop.createlistmodule.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.major.shop.R;
import com.major.shop.logic.ListOperLogic;
import com.major.shop.logic.ProductOperationLogic;
import com.major.shop.model.BList;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Sergey on 13.01.14.
 */
public class ListCustomAdapter extends BaseAdapter {
  //===================================================
  ListActivityC       activity      = null;
  LayoutInflater      lInflater     = null;
  ArrayList<BList>    objects       = null;
  Button              btnDeleteItem = null;
  int                 numb          = 0   ;
  private String      listItemParam = null;
  private Integer     mSelectedItem = null;
  private static long back_pressed;
  private static int  idListForDelete;
  //===================================================
  //---------------------------------------------------
  public ListCustomAdapter(ListActivityC _activity, ArrayList <BList> _list, String _listItemParam) {
	activity      = _activity ;
    listItemParam = _listItemParam;
    if(_list != null) objects = _list;
    
    lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }
  //---------------------------------------------------
  public int getSelectedItem() {
      return mSelectedItem;
  }
  //---------------------------------------------------
  public void setSelectedItem(int selectedItem) {
    mSelectedItem = selectedItem;
    notifyDataSetChanged();
  }
  //---------------------------------------------------
  @Override
  public int getCount() { return objects.size(); }
  //---------------------------------------------------
  @Override
  public Object getItem(int position) { return objects.get(position); }
  //---------------------------------------------------
  @Override
  public long getItemId(int position) { return position; }
  //---------------------------------------------------
  @Override
  public View getView(int position, View _convertView, ViewGroup parent) {
	View view = _convertView;
	if (view == null) 
	  view = lInflater.inflate(R.layout.list_item, parent, false);
    
    final BList p = getList(position);
    ((TextView)view.findViewById(R.id.listNameLst)).setText(p.listName  );
    final ImageView btn = (ImageView)view.findViewById(R.id.deleteButton);
    ImageView iv = (ImageView) view.findViewById(R.id.imgListTypeL);
    if     ("C".equalsIgnoreCase(p.status)) iv.setImageResource(R.drawable.newlst    );	
    else if("R".equalsIgnoreCase(p.status)) iv.setImageResource(R.drawable.inboxlst  );
    else if("S".equalsIgnoreCase(p.status)) iv.setImageResource(R.drawable.outboxlst );
    else if("X".equalsIgnoreCase(p.status)) iv.setImageResource(R.drawable.finishlst );
    if(mSelectedItem != null){
      if (position == mSelectedItem) {
    	view.setBackgroundColor(activity.getResources().getColor(R.color.select_item));
      } else {
    	view.setBackgroundColor(activity.getResources().getColor(R.color.t1));
      }
    }
    btn.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (back_pressed + 2000 > System.currentTimeMillis() && idListForDelete == p.listId) {
          new ListOperLogic(activity).deleteList(p.listId);
          activity.loadAllLists();
          setSelectedItem(-1);
          activity.productsList.clear();
          activity.pca.notifyDataSetChanged();
          notifyDataSetChanged();
        }else{
          Toast.makeText(activity.getBaseContext(), R.string.doubleClickDelete, Toast.LENGTH_SHORT).show();
          idListForDelete=p.listId;
        }
          back_pressed = System.currentTimeMillis();

      }
    });
    return view;
  }
  //---------------------------------------------------
  public BList getList(int position) { return ((BList) getItem(position)); }
  //---------------------------------------------------
  ArrayList<BList> getBox() {
    ArrayList<BList> box = new ArrayList<BList>();
    return box;
  }
  //---------------------------------------------------
  public void setList(ArrayList <BList> _list){
	objects = _list;
  }
  //---------------------------------------------------  
}
