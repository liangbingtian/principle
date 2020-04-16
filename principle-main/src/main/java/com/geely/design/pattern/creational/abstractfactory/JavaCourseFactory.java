package com.geely.design.pattern.creational.abstractfactory;

/**
 * liangbingtian 2019/5/22 下午11:14
 */
public class JavaCourseFactory implements CourseFactory{

  public Article getArticle() {
    return new JavaArticle();
  }

  public Video getVideo() {
    return new JavaVideo();
  }
}
