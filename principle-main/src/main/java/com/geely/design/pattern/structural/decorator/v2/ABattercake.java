package com.geely.design.pattern.structural.decorator.v2;

import com.geely.design.pattern.structural.decorator.common.IBattercake;

/**
 * 2019/11/5 上午12:31
 *
 * @author liangbingtian
 */
public abstract class ABattercake implements IBattercake {

  protected abstract String getDesc();
  protected abstract int cost();

}
