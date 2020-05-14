package com.mall.concurrency.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.mall.concurrency.annoations.ThreadSafe;
import java.util.Collections;
import java.util.Map;

/**
 * Created by liangbingtian on 2020/4/19 4:03 下午
 */
@ThreadSafe
public class ImmutableExample3 {

  private static final ImmutableList list = ImmutableList.of(1, 2, 3);

  private final static ImmutableSet set = ImmutableSet.copyOf(list);

  private static final ImmutableMap<String, String> map = ImmutableMap.<String, String>builder().put("1", "1").build();

  public static void main(String[] args) {
    list.add(4);
  }
}
