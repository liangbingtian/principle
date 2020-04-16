package com.geely.design.principle.dependenceinversion;

/**
 * liangbingtian 2019/5/18 下午11:33
 */
public class Geely {

  private ICourse iCourse;

  public void setiCourse(ICourse iCourse) {
    this.iCourse = iCourse;
  }

  public void studyImoocCourse() {
    iCourse.studyCourse();
  }

}
