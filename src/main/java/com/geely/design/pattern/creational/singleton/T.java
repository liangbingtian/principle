package com.geely.design.pattern.creational.singleton;

/**
 * 2019/7/11 下午3:29
 *
 * @author liangbingtian
 */
public class T implements Runnable{

  public void run() {
    //懒汉模式
    LazySingleton lazySingleton = LazySingleton.getInstance();
    System.out.println(Thread.currentThread().getName()+" "+lazySingleton);
  }
}
