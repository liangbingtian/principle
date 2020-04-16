package com.geely.design.threadLocal;

/**
 * Created by liangbingtian on 2020/2/27 8:35 下午
 */
public class Val<T> {

  T v;

  public void set(T _v) {
    v = _v;
  }

  public T get() {
    return v;
  }

}
