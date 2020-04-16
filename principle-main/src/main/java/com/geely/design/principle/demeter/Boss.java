package com.geely.design.principle.demeter;



/**
 * liangbingtian 2019/5/19 下午10:27
 */
public class Boss {

  public void commandCheckNumber(TeamLeader teamLeader) {

    teamLeader.checkNumberOfCourses();
  }

}
