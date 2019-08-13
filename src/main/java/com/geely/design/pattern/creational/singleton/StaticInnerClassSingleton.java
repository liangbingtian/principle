package com.geely.design.pattern.creational.singleton;

/**
 * 2019/7/14 下午10:54
 *
 * @author liangbingtian
 */
public class StaticInnerClassSingleton {

  private static class InnerClass {
    private static StaticInnerClassSingleton staticInnerClassSingleton = new StaticInnerClassSingleton();
  }

  public static StaticInnerClassSingleton getInstance() {
    return InnerClass.staticInnerClassSingleton;
  }

  private StaticInnerClassSingleton(){
    //防止反射攻击的方法,此种防止反射的方法适用于在类加载的时候就完成对象创建的类。
    if (InnerClass.staticInnerClassSingleton != null) {
      throw new RuntimeException("单例构造器禁止反射调用");
    }
  }

}
