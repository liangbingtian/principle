package com.geely.design.pattern.creational.factorymethod;

/**
 * liangbingtian 2019/5/22 下午10:28
 */
public class PythonVideoFactory extends VideoFactory{

  public Vedio getVideo() {
    return new PythonVedio();
  }
}
