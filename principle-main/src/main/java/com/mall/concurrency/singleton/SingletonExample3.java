package com.mall.concurrency.singleton;

import com.mall.concurrency.annoations.NotRecommend;
import com.mall.concurrency.annoations.NotThreadSafe;
import com.mall.concurrency.annoations.ThreadSafe;

/**
 * Created by liangbingtian on 2020/4/18 9:19 下午
 * 懒汉模式：单例的实例在第一次使用的时候进行创建
 */
@ThreadSafe
@NotRecommend
public class SingletonExample3 {

  /**
   * 如何保证一个类只能初始化一次，那么就要保证这个类不能随便new一个新的对象出来。
   * 那么就要设置该类的私有构造方法，只有构造方法私有了
   * ，外边才不能一直new这个对象出来
   */
  private SingletonExample3() {

  }

  //单例对象
  private static SingletonExample3 instance = null;

  //通过静态工厂方法来获取单例对象
  //在方法上加synchronized保证了线程安全，但是带来了性能上的开销
  public static synchronized SingletonExample3 getInstance() {
    if (instance == null) {
      instance = new SingletonExample3();
    }
    return instance;
  }

}
