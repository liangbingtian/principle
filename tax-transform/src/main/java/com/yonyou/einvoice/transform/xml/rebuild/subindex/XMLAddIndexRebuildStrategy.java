package com.yonyou.einvoice.transform.xml.rebuild.subindex;

import com.yonyou.einvoice.transform.xml.rebuild.AbstractRebuildStrategy;
import com.yonyou.einvoice.transform.xml.rebuild.XMLTreeNode;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

/**
 * xml重建器。用于对指定xml标签下的子标签添加索引列表
 * @author liuqiangm
 */
public class XMLAddIndexRebuildStrategy extends AbstractRebuildStrategy {

  public XMLAddIndexRebuildStrategy(String rebuildPath) {
    super(rebuildPath);
  }

  @Override
  public void treeRebuild(XMLTreeNode treeNode, String path) {
    if (StringUtils.isBlank(path)) {
      path = treeNode.getKey();
    } else {
      path += "." + treeNode.getKey();
    }
    if (this.getRebuildPath().startsWith(path) && !this.getRebuildPath().equals(path)) {
      for (XMLTreeNode subXMLTreeNode : treeNode.getSunNodeList()) {
        treeRebuild(subXMLTreeNode, path);
      }
    } else if (this.getRebuildPath().equals(path)) {
      reviseXMLTreeNode(treeNode);
    }
  }

  private void reviseXMLTreeNode(XMLTreeNode treeNode) {
    Iterator<XMLTreeNode> iterator = treeNode.getSunNodeList().iterator();
    int index = 1;
    while (iterator.hasNext()) {
      XMLTreeNode subNode = iterator.next();
      subNode.setKey(subNode.getKey() + "_" + index);
      index++;
    }
  }
}
