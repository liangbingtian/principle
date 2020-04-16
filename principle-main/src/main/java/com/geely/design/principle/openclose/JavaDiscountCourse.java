package com.geely.design.principle.openclose;

/**
 * liangbingtian 2019/5/18 下午10:57
 */
public class JavaDiscountCourse extends JavaCourse {

  public JavaDiscountCourse(Integer id, String name, Double price) {
    super(id, name, price);
  }

  public Double getDiscontPrice() {
    return super.getPrice() * 0.8;
  }

}
