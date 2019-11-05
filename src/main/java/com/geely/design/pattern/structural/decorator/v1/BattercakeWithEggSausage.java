package com.geely.design.pattern.structural.decorator.v1;

/**
 * 2019/11/5 上午12:22
 *
 * @author liangbingtian
 */
public class BattercakeWithEggSausage extends BattercakeWithEgg {

  @Override
  public String getDesc() {
    return super.getDesc()+" 加一根香肠";
  }

  @Override
  public int cost() {
    return super.cost()+2;
  }
}
