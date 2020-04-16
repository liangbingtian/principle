package com.yonyou.einvoice.transform.xml.processor;

/**
 * xml节点的消费者
 *
 * @author liuqiangm
 */
public abstract class AbstractXmlConsumer {

  /**
   * 消费开始
   */
  public void start() {

  }

  /**
   * 获取到指定xml路径下的xml之后，真正执行处理逻辑的consume方法
   *
   * @param object
   */
  protected abstract void consume(Object object);

  /**
   * 消费结束
   */
  public void end() {

  }

  /**
   * xml节点的路径。以：a.b.c为格式
   */
  public abstract String getXmlPath();
}
