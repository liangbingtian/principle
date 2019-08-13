package com.geely.design.pattern.creational.abstractfactory;

/**
 * liangbingtian 2019/5/22 下午11:40
 */
public class Test {

  public static void main(String[] args) {
    CourseFactory courseFactory = new JavaCourseFactory();
    Video video = courseFactory.getVideo();
    Article article = courseFactory.getArticle();
    video.produce();
    article.produce();
  }

}
