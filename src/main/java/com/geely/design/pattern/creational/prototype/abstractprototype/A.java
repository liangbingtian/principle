package com.geely.design.pattern.creational.prototype.abstractprototype;

/**
 * 2019/11/4 下午9:08
 *
 * @author liangbingtian
 */
public abstract class A implements Cloneable{

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
