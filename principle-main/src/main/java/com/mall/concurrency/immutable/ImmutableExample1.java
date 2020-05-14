package com.mall.concurrency.immutable;

import com.google.common.collect.Maps;
import com.mall.concurrency.annoations.NotThreadSafe;
import java.util.Map;

/**
 * Created by liangbingtian on 2020/4/19 4:03 下午
 */
@NotThreadSafe
public class ImmutableExample1 {

  private static final Integer a = 1;
  private static final String b = "2";
  private static final Map<Integer, Integer> map = Maps.newHashMap();

  static {
    map.put(1, 2);
    map.put(3, 4);
    map.put(5, 6);
  }

  public static void main(String[] args) {
    map.put(1,3);
  }

  private void test(final int a) {
//    a = 2;
  }



}
