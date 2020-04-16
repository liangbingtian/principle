package com.geely.design.threadLocal;

import java.util.HashMap;
import java.util.HashSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liangbingtian on 2020/2/27 8:11 下午
 */
@Controller
public class StatController {

  //add有问题。因为set会存在于任何一个线程当中。他处于临界区
  static HashSet<Val<Integer>> set = new HashSet<>();

  static ThreadLocal<Val<Integer>> c = new ThreadLocal<Val<Integer>>() {
    @Override
    protected Val<Integer> initialValue() {
      Val<Integer> v = new Val<>();
      v.set(0);
      addSet(v);
      return v;
    }
  };

  private static synchronized void addSet(Val<Integer> v) {
    set.add(v);
  }

  void _add() throws InterruptedException {
    Thread.sleep(100);
    Val<Integer> val = c.get();
    val.set(val.get() + 1);
  }

  @RequestMapping("/stat")
  public Integer stat() {
    Integer integer = set.stream().map(Val::get).reduce(Integer::sum).get();
    return integer;
  }

  @RequestMapping("/add")
  public Integer add() throws InterruptedException {
    _add();
    return 1;
  }


}
