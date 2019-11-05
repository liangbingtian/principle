package com.geely.design.pattern.structural.decorator.v2;

/**
 * 2019/11/5 下午9:45
 *
 * @author liangbingtian
 */
public class EggDecorator extends AbstractDecorator{

  public EggDecorator(ABattercake aBattercake) {
    super(aBattercake);
  }

  @Override
  protected void doSomething() {

  }

  @Override
  protected String getDesc() {
    return super.getDesc()+" 加一个鸡蛋";
  }

  @Override
  protected int cost() {
    return super.cost()+1;
  }
}
