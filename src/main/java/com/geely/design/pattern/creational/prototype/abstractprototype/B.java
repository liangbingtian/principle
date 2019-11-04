package com.geely.design.pattern.creational.prototype.abstractprototype;

/**
 * 2019/11/4 下午9:09
 *
 * @author liangbingtian
 * B 继承A也有克隆的功能
 */
public class B extends A {

  public static void main(String[] args) throws CloneNotSupportedException {
    B b = new B();
    b.clone();
  }
}
