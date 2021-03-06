package com.geely.design.pattern.creational.simplefactory;

/**
 * liangbingtian 2019/5/21 上午12:42
 */
public class Test {

  public static void main(String[] args) {
//    VideoFactory factory = new VideoFactory();
//    Vedio vedio = factory.getVedio("java");
//    if (vedio == null){
//      return;
//    }
//    vedio.produce();

    VideoFactory videoFactory = new VideoFactory();
    Vedio vedio = videoFactory.getVedio(JavaVedio.class);
    if (vedio == null) {
      return;
    }
    vedio.produce();
  }

}
