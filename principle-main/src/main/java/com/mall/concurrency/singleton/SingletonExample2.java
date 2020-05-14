package com.mall.concurrency.singleton;

import com.mall.concurrency.annoations.NotThreadSafe;
import com.mall.concurrency.annoations.ThreadSafe;

/**
 * Created by liangbingtian on 2020/4/18 9:19 下午
 * 饿汉模式：单例的实例在类装载的时候进行创建
 * 饿汉模式的使用：1.其私有构造方法没有太多的实现。2.其创建出来的资源没有被浪费。
 */
@NotThreadSafe
@ThreadSafe
public class SingletonExample2 {

  /**
   * 如何保证一个类只能初始化一次，那么就要保证这个类不能随便new一个新的对象出来。
   * 那么就要设置该类的私有构造方法，只有构造方法私有了
   * ，外边才不能一直new这个对象出来
   */
  private SingletonExample2() {

  }

  //单例对象
  private static SingletonExample2 instance = new SingletonExample2();

  //通过静态工厂方法来获取单例对象
  public static SingletonExample2 getInstance() {
    return instance;
  }

}
