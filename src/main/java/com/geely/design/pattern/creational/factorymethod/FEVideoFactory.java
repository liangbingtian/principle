package com.geely.design.pattern.creational.factorymethod;

/**
 * liangbingtian 2019/5/22 下午10:36
 */
public class FEVideoFactory extends VideoFactory{

  public Vedio getVideo() {
    return new FEVideo();
  }
}
