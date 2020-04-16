package com.yonyou.einvoice.transform.xml.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * xml节点的消费者
 *
 * @author liuqiangm
 */
public abstract class AbstractXmlBatchConsumer extends AbstractXmlConsumer {

  List<Object> batchList = null;

  @Override
  public void start() {
    int batchSize = batchSize();
    batchList = new ArrayList<>(batchSize);
  }

  /**
   * 获取到指定xml路径下的xml之后，真正执行处理逻辑的consume方法
   *
   * @param object
   */
  @Override
  public void consume(Object object) {
    batchList.add(object);
    if (batchList.size() == batchSize()) {
      batchConsume(batchList);
      batchList.clear();
    }
  }

  /**
   * 指定获取批量数据后，该如何处理
   *
   * @param batchList
   */
  public abstract void batchConsume(List<Object> batchList);

  /**
   * 消费结束
   */
  @Override
  public void end() {
    if (!batchList.isEmpty()) {
      batchConsume(batchList);
      batchList.clear();
    }
  }

  public abstract int batchSize();
}
