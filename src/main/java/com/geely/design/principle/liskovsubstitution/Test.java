package com.geely.design.principle.liskovsubstitution;

/**
 * liangbingtian 2019/5/19 下午11:05
 */
public class Test {

  public static void resize(Rectangle rectangle) {
    while (rectangle.getWidth() < rectangle.getLength()) {
      rectangle.setWidth(rectangle.getWidth() + 1);
      System.out.println("width:" + rectangle.getWidth() + "length:" + rectangle.getLength());
    }
    System.out.println("width:" + rectangle.getWidth() + "length:" + rectangle.getLength());
  }

  public static void main(String[] args) {
    Rectangle rectangle = new Rectangle();
    rectangle.setWidth(10);
    rectangle.setLength(20);
    resize(rectangle);
  }

}
