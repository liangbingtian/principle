package com.geely.design.pattern.creational.factorymethod;

/**
 * liangbingtian 2019/5/22 下午10:27
 */
public class JavaVideoFactory extends VideoFactory{

  public Vedio getVideo() {
    return new JavaVedio();
  }
}
