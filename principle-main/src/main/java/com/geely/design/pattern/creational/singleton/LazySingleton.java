package com.geely.design.pattern.creational.singleton;

/**
 * 2019/7/11 下午3:19
 *
 * @author liangbingtian
 */
public class LazySingleton {

  private static LazySingleton lazySingleton = null;

  private LazySingleton(){

  }

  public synchronized static LazySingleton getInstance(){
    if (lazySingleton == null) {
      lazySingleton = new LazySingleton();
    }
    return lazySingleton;
  }

}
