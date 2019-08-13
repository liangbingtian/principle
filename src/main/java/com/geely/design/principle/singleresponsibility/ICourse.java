package com.geely.design.principle.singleresponsibility;

/**
 * liangbingtian 2019/5/19 下午5:41
 */
public interface ICourse {

  //获取课程信息与课程相关管理。这两种操作相互影响。所以可以将其拆开。
  String getCourseName();
  byte[] getCourseVideo();

  void studyCourse();
  void refundCourse();

}
