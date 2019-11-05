package com.geely.design.pattern.creational.singleton;

import com.geely.design.pattern.structural.decorator.common.IBattercake;
import com.geely.design.pattern.structural.decorator.v1.Battercake;
import com.geely.design.pattern.structural.decorator.v1.BattercakeWithEgg;
import com.geely.design.pattern.structural.decorator.v1.BattercakeWithEggSausage;
import com.geely.design.pattern.structural.decorator.v2.ABattercake;
import com.geely.design.pattern.structural.decorator.v2.EggDecorator;
import com.geely.design.pattern.structural.decorator.v2.SausageDecorator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;

/**
 * 2019/11/5 上午12:23
 *
 * @author liangbingtian
 */
public class DecoratorTest {

  /**
   * 不用装饰者模式的做法。
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  @Test
  public void test1()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Battercake battercake = new Battercake();
    Battercake battercakeWithEgg = new BattercakeWithEgg();
    Battercake battercakeWithEggSausage = new BattercakeWithEggSausage();
    System.out.println(getResult(battercake));
    System.out.println(getResult(battercakeWithEgg));
    System.out.println(getResult(battercakeWithEggSausage));
  }


  private String getResult(IBattercake battercake)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method1 = battercake.getClass().getDeclaredMethod("cost");
    Method method2 = battercake.getClass().getDeclaredMethod("getDesc");
    method1.setAccessible(true);
    method2.setAccessible(true);
    return method2.invoke(battercake)+" 销售价格为:"+method1.invoke(battercake);
  }


  /**
   * 修改成装饰器模式之后的做法
   */
  @Test
  public void test2()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    ABattercake aBattercake;
    aBattercake = new com.geely.design.pattern.structural.decorator.v2.Battercake();
    aBattercake = new EggDecorator(aBattercake);
    aBattercake = new EggDecorator(aBattercake);
    aBattercake = new SausageDecorator(aBattercake);
    System.out.println(getResult(aBattercake));
  }

}
