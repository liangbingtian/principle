package com.geely.design.pattern.creational.singleton;

import java.io.Serializable;

/**
 * 2019/7/15 下午9:21
 *
 * @author liangbingtian
 */
public class HungrySingleton implements Serializable,Cloneable {

  //在类加载的时候就对其进行初始化
  private static final HungrySingleton hungrySingleton = new HungrySingleton();

  public static HungrySingleton getInstance() {
    return hungrySingleton;
  }

  public Object readResolve() {
    return hungrySingleton;
  }

  private HungrySingleton() {
    //防止反射攻击的方法,此种防止反射的方法适用于在类加载的时候就完成对象创建的类。
    if (hungrySingleton != null) {
      throw new RuntimeException("单例构造器禁止反射调用");
    }
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
