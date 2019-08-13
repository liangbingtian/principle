package com.geely.design.pattern.creational.singleton;

/**
 * 2019/7/11 下午4:22
 *
 * @author liangbingtian
 */
public class LazyDoubleCheckSingleton {

  //volatile关键字可以为我们实现线程安全的延迟初始化,这样重排序就会被禁止,cpu有共享内存,
  //加了该关键字后，所有线程都可以看见共享内存的状态。保证了内存的可见性。
  //加了这个之后就不允许下边注释的步骤重排序。
  private volatile static LazyDoubleCheckSingleton lazyDoubleCheckSingleton = null;

  private LazyDoubleCheckSingleton() {

  }

  public static LazyDoubleCheckSingleton getInstance() {
    if (lazyDoubleCheckSingleton == null) {
      synchronized (LazyDoubleCheckSingleton.class) {
        if (lazyDoubleCheckSingleton == null) {
          lazyDoubleCheckSingleton = new LazyDoubleCheckSingleton();
          //1.分配内存给这个对象
          //2.初始化对象
          //3.设置lazyDoubleCheckSingleton 指向刚分配的内存
        }
      }
    }
    return lazyDoubleCheckSingleton;
  }

}
