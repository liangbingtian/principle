package com.mall.concurrency.singleton;

import com.mall.concurrency.annoations.NotThreadSafe;

/**
 * Created by liangbingtian on 2020/4/18 9:19 下午
 * 懒汉模式：单例的实例在第一次使用的时候进行创建
 */
@NotThreadSafe
public class SingletonExample1 {

  /**
   * 如何保证一个类只能初始化一次，那么就要保证这个类不能随便new一个新的对象出来。
   * 那么就要设置该类的私有构造方法，只有构造方法私有了
   * ，外边才不能一直new这个对象出来
   */
  private SingletonExample1() {

  }

  //单例对象
  private static SingletonExample1 instance = null;

  //通过静态工厂方法来获取单例对象
  public static SingletonExample1 getInstance() {
    if (instance == null) {
      instance = new SingletonExample1();
    }
    return instance;
  }

}
