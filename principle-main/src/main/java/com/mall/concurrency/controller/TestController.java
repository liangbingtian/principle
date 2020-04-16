package com.mall.concurrency.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liangbingtian on 2020/4/9 9:49 下午
 */
@Controller
@Slf4j
public class TestController {

  @RequestMapping("/test")
  @ResponseBody
  public String test() {
    return "test";
  }

}
