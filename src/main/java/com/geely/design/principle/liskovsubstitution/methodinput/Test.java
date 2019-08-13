package com.geely.design.principle.liskovsubstitution.methodinput;

import java.util.HashMap;

/**
 * liangbingtian 2019/5/19 下午11:24
 */
public class Test {

  public static void main(String[] args) {
    Child child = new Child();
    HashMap hashMap = new HashMap();
    child.method(hashMap);
  }

}
