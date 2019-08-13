package com.geely.design.principle.demeter;

/**
 * liangbingtian 2019/5/19 下午10:31
 */
public class Test {

  public static void main(String[] args) {
    Boss boss = new Boss();
    TeamLeader teamLeader = new TeamLeader();
    boss.commandCheckNumber(teamLeader);
  }

}
