package com.mall.concurrency.immutable;

import com.google.common.collect.Maps;
import com.mall.concurrency.annoations.NotThreadSafe;
import com.mall.concurrency.annoations.ThreadSafe;
import java.util.Collections;
import java.util.Map;

/**
 * Created by liangbingtian on 2020/4/19 4:03 下午
 */
@ThreadSafe
public class ImmutableExample2 {

  private static final Integer a = 1;
  private static final String b = "2";
  private static Map<Integer, Integer> map = Maps.newHashMap();

  static {
    map.put(1, 2);
    map.put(3, 4);
    map.put(5, 6);
    map = Collections.unmodifiableMap(map);
  }

}
