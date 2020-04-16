package com.geely.design.threadLocal;

/**
 * Created by liangbingtian on 2020/2/23 8:52 下午
 */
public class Basic {

  //ThreadLocal<T>
  public static ThreadLocal x = ThreadLocal.withInitial(() -> {
    System.out.println("Inital Value run..");
    return Thread.currentThread().getId();
  });

  //不同的线程访问ThreadLocal实例中保存的变量是不同的
  public static void main(String[] args) {
    new Thread(() -> System.out.println(x.get())).start();
    x.set(107L);
    x.remove();
    System.out.println(x.get());
  }

}
