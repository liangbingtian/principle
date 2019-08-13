package com.geely.design.principle.openclose;

import java.math.BigDecimal;

/**
 * liangbingtian 2019/5/18 下午10:45
 */
public class Test {

  public static void main(String[] args) {
    ICourse javaCourse = new JavaDiscountCourse(96, "Java从零到企业级电商开发", 348d);


    System.out.println("课程ID:" + javaCourse.getId() + "课程名称:" + javaCourse.getName() + "课程价格:"
        + javaCourse.getPrice() + "课程折扣后的价格:" + ((JavaDiscountCourse) javaCourse).getDiscontPrice());
  }

}
