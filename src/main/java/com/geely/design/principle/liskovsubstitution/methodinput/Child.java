package com.geely.design.principle.liskovsubstitution.methodinput;

import java.util.HashMap;
import java.util.Map;

/**
 * liangbingtian 2019/5/19 下午11:23
 */
public class Child extends Base{

  @Override
  public void method(HashMap map) {
    System.out.println("子类被执行");
  }

  public void method(Map map) {
    System.out.println("子类被执行");
  }
}
