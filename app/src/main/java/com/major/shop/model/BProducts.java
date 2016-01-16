package com.major.shop.model;

import java.util.ArrayList;

/**
 * Created by Sergey on 23.12.13.
 */
public class BProducts {
  //==============================================================
  public ArrayList <BProduct> products = null;
  //==============================================================
  //--------------------------------------------------------------
  public BProducts(){
    products = new ArrayList<BProduct>();
  }
  //--------------------------------------------------------------
  public BProducts(String _str){ }
}
