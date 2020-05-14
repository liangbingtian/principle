package com.mall.concurrency.singleton;

import com.mall.concurrency.annoations.NotThreadSafe;
import com.mall.concurrency.annoations.Recommend;
import com.mall.concurrency.annoations.ThreadSafe;

/**
 * Created by liangbingtian on 2020/4/18 9:19 下午
 * 枚举模式是最安全的
 */
@ThreadSafe
@Recommend
public class SingletonExample7 {

  /**
   * 如何保证一个类只能初始化一次，那么就要保证这个类不能随便new一个新的对象出来。
   * 那么就要设置该类的私有构造方法，只有构造方法私有了
   * ，外边才不能一直new这个对象出来
   */
  private SingletonExample7() {

  }
  
  //通过静态工厂方法来获取单例对象
  public static SingletonExample7 getInstance() {
    return Singleton.INSTANCE.getSingleton();
  }

  private enum Singleton {
    INSTANCE;

    private SingletonExample7 singleton;

    //JVM保证这个方法只被调用一次。
    Singleton() {
      singleton = new SingletonExample7();
    }

    public SingletonExample7 getSingleton() {
      return singleton;
    }
  }

}
