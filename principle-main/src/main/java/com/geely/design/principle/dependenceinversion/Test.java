package com.geely.design.principle.dependenceinversion;

/**
 * liangbingtian 2019/5/18 下午11:36
 */
public class Test {

//  public static void main(String[] args) {
//    Geely geely = new Geely();
//    geely.studyImoocCourse(new JavaCourse());
//    geely.studyImoocCourse(new FECourse());
//  }

  public static void main(String[] args) {
    Geely geely = new Geely();
    geely.setiCourse(new JavaCourse());
    geely.studyImoocCourse();

    geely.setiCourse(new FECourse());
    geely.studyImoocCourse();
  }
}
