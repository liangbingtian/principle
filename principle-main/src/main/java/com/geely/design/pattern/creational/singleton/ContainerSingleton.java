package com.geely.design.pattern.creational.singleton;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * 2019/9/30 下午1:50
 *
 * @author liangbingtian
 */
public class ContainerSingleton {

  private ContainerSingleton() {

  }

  private static Map<String, Object> singletonMap = new HashMap<String, Object>();
  public static void putInstance(String key, Object instance) {
    if (StringUtils.isNotBlank(key) && instance!=null) {
      if (!singletonMap.containsKey(key)) {
        singletonMap.put(key, instance);
      }
    }
  }
  public static Object getInstance(String key) {
    return singletonMap.get(key);
  }

}
