package com.geely.design.principle.demeter;

import java.util.ArrayList;
import java.util.List;

/**
 * liangbingtian 2019/5/19 下午10:27
 */
public class TeamLeader {

  public void checkNumberOfCourses() {
    List<Course> courseList = new ArrayList<Course>();
    for (int i = 1; i < 20; i++) {
      courseList.add(new Course());
    }
    System.out.println("在线课程的数量为:" + courseList.size());
  }

}
