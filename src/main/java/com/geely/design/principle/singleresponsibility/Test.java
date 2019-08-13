package com.geely.design.principle.singleresponsibility;

/**
 * liangbingtian 2019/5/19 上午12:43
 */
public class Test {

  public static void main(String[] args) {
      FlyBird bird = new FlyBird();
      WalkBird bird1 = new WalkBird();
      bird.mainMoveMode("大雁");
      bird1.mainMoveMode("企鹅");

  }

}
