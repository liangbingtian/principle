package com.geely.design.principle.liskovsubstitution.methodoutput;

import java.util.HashMap;

/**
 * liangbingtian 2019/5/19 下午11:47
 */
public class Child extends Base{

  public HashMap method() {
    HashMap hashMap = new HashMap();
    System.out.println("子类method被执行");
    hashMap.put("message", "子类method被执行");
    return hashMap;
  }
}
