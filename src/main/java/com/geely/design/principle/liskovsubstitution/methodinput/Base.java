package com.geely.design.principle.liskovsubstitution.methodinput;

import java.util.HashMap;

/**
 * liangbingtian 2019/5/19 下午11:21
 */
public class Base {
  public void method(HashMap map){
    System.out.println("父类被执行");
  }

}
