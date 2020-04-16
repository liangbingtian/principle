package com.geely.design.pattern.structural.decorator.v2;

/**
 * 2019/11/5 下午9:41
 *
 * @author liangbingtian
 *
 * 改成抽象的装饰器是有必要的。看具体的业务。子装饰器是否需要实现某些独有的方法
 */
public abstract class AbstractDecorator extends ABattercake{

  private ABattercake aBattercake;

  public AbstractDecorator(ABattercake aBattercake) {
    this.aBattercake = aBattercake;
  }

  protected abstract void doSomething();

  @Override
  protected String getDesc() {
    return this.aBattercake.getDesc();
  }

  @Override
  protected int cost() {
    return this.aBattercake.cost();
  }
}
