package com.major.shop.createlistmodule.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.major.shop.R;
import com.major.shop.logic.ProductOperationLogic;
import com.major.shop.model.BList;
import com.major.shop.model.BProduct;
import com.major.shop.model.BProducts;

public class ProductCustomAdapter extends BaseAdapter {
  //===================================================
  ListActivityC        activity   ;
  LayoutInflater       lInflater     = null;
  LinkedList <BProduct> list          = null;
  Button               btnDeleteItem = null;
  public int                  numb          = 0   ;
  protected ProductCustomAdapter THIS       ;
  //===================================================
  //---------------------------------------------------
  public ProductCustomAdapter(ListActivityC _activity, LinkedList <BProduct> _prodList) {
	THIS     = this;
	activity = _activity;
    if(_prodList != null) list = _prodList;
    
    lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }
  //---------------------------------------------------
  @Override
  public int getCount() { return list.size(); }
  //---------------------------------------------------
  @Override
  public Object getItem(int position) { return list.get(position); }
  //---------------------------------------------------
  @Override
  public long getItemId(int position) { return position; }
  //---------------------------------------------------
  @Override
  public View getView(final int position, View _convertView, ViewGroup parent) {
	View view = _convertView;
	if (view == null) 
	  view = lInflater.inflate(R.layout.product_item, parent, false);
	    
    BProduct p = getList(position);
    ((TextView ) view.findViewById(R.id.productName)).setText(String.valueOf(p.productName));
    ((TextView ) view.findViewById(R.id.comment    )).setText(p.comment     );
    final ImageView image = (ImageView)view.findViewById(R.id.checkButton);
    if("Y".equalsIgnoreCase(p.isBay))image.setImageResource(R.drawable.finishlst);
    else          image.setImageResource(R.drawable.nocheck);

    final BProduct pr = p;
    final View vv = view;
    image.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          if("Y".equalsIgnoreCase(pr.isBay)) pr.isBay = "N";
          else pr.isBay = "Y";
          image.setImageResource("Y".equalsIgnoreCase(pr.isBay)? R.drawable.finishlst : R.drawable.nocheck);
          new ProductOperationLogic(activity).saveStatusProduct(pr.listId, pr);
          numb = position;
          if(position != 0 || (list!= null && list.size()>1))
            vv.startAnimation(activity.anim_out);
          activity.loadAllLists();
        }
      });
	    	
	    
    return view;
  }
  //---------------------------------------------------
  public void changePositionElementList(){
	View v = null;
	BProduct prod = list.get(numb);
	list.remove(numb);
	if("Y".equalsIgnoreCase(prod.isBay)){
	  list.add(list.size(), prod);
	  v = activity.prodListView.getChildAt(list.size()-1);
	}
	else{
	  list.add(0, prod);
      v = activity.prodListView.getChildAt(0);
    }
	THIS.notifyDataSetChanged();
	if(v!= null)
	  v.startAnimation(activity.anim_in);
  }
  //---------------------------------------------------
  public BProduct getList(int position) { return ((BProduct) getItem(position)); }
  //---------------------------------------------------
  ArrayList<BList> getBox() {
    ArrayList<BList> box = new ArrayList<BList>();
    return box;
  }
  //---------------------------------------------------
  public void setList(LinkedList <BProduct> _list){
	list = _list;
  }
  //---------------------------------------------------  
}
