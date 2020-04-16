package com.geely.design.pattern.creational.abstractfactory;

/**
 * liangbingtian 2019/5/22 下午11:26
 */
public class PythonCourseFactory implements CourseFactory{

  public Article getArticle() {
    return new PythonArticle();
  }

  public Video getVideo() {
    return new PythonVideo();
  }
}
