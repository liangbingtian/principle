package com.yonyou.einvoice.transform.xml.rebuild.rowtocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.xml.rebuild.AbstractRebuildStrategy;
import com.yonyou.einvoice.transform.xml.rebuild.XMLTreeNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * xml重建器策略。用于实现xml某节点的行列转换
 * @author liuqiangm
 */
public class XMLRowToColRebuildStrategy extends AbstractRebuildStrategy {

  private Set<String> discardNodeStrSet = new TreeSet<>();

  private Map<String, Map<String, String>> tagSubNodeMap = new TreeMap<>();
  private LinkedList<XMLTreeNode> treeNodes = new LinkedList<>();

  public String rebuildPath;

  private boolean addIndex = true;

  public XMLRowToColRebuildStrategy(String rebuildPath) {
    super(rebuildPath);
  }

  public XMLRowToColRebuildStrategy(String rebuildPath, boolean addIndex) {
    super(rebuildPath);
    this.addIndex = addIndex;
  }

  public XMLRowToColRebuildStrategy(String rebuildPath, Set<String> discardNodeStrSet) {
    this(rebuildPath);
    this.discardNodeStrSet.addAll(discardNodeStrSet);
  }

  public XMLRowToColRebuildStrategy(String rebuildPath, Set<String> discardNodeStrSet,
      Map<String, Map<String, String>> tagSubNodeMap) {
    this(rebuildPath, discardNodeStrSet);
    this.tagSubNodeMap.putAll(tagSubNodeMap);
  }

  public XMLRowToColRebuildStrategy(String rebuildPath, Map<String, Map<String, String>> tagSubNodeMap) {
    this(rebuildPath);
    this.tagSubNodeMap = tagSubNodeMap;
  }

  public XMLRowToColRebuildStrategy(String rebuildPath, InputStream inputStream) {
    this(rebuildPath);
    if (inputStream != null) {
      try {
        tagSubNodeMapAdd(JSON.parseObject(inputStream, JSONObject.class));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public XMLRowToColRebuildStrategy(String rebuildPath, String jsonStr) {
    this(rebuildPath);
    tagSubNodeMapAdd(JSON.parseObject(jsonStr));
  }

  private void tagSubNodeMapAdd(JSONObject jsonObject) {
    for (String key : jsonObject.keySet()) {
      Map<String, String> keyMap = new HashMap<>();
      tagSubNodeMap.put(key, keyMap);
      JSONObject keyObject = jsonObject.getJSONObject(key);
      for (String key1 : keyObject.keySet()) {
        keyMap.put(key1, keyObject.getString(key1));
      }
    }
  }

  @Override
  public void treeRebuild(XMLTreeNode treeNode, String path) {
    if (path == null || "".equals(path)) {
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

  public void reviseXMLTreeNode(XMLTreeNode treeNode) {
    LinkedList<XMLTreeNode> treeNodeLayer = new LinkedList<>(treeNode.getSunNodeList());
    treeNode.getSunNodeList().clear();
    while (true) {
      if (treeNodeLayer.size() != 0 && treeNodeLayer.peekFirst().getSunNodeList().size() != 0) {
        XMLTreeNode subNode = new XMLTreeNode();
        boolean flag = false;
        int subIndex = 1;
        for (XMLTreeNode node : treeNodeLayer) {
          XMLTreeNode tmpSubNode = node.getSunNodeList().pollFirst();
          subNode.setKey(tmpSubNode.getKey());
          String key = node.getKey() + "_" + subIndex;
          if (!addIndex) {
            key = node.getKey();
          }
          tmpSubNode.setKey(key);
          subIndex++;
          if (discardNodeStrSet.contains(tmpSubNode.getKey())) {
            flag = true;
          }
          subNode.getSunNodeList().add(tmpSubNode);
        }
        if (!flag) {
          Map<String, String> subTagNodeMap = tagSubNodeMap.getOrDefault(subNode.getKey(),
              Collections.emptyMap());
          for (Map.Entry<String, String> subTagNode : subTagNodeMap.entrySet()) {
            XMLTreeNode subTagXMLTreeNode = new XMLTreeNode(subTagNode.getKey());
            subTagXMLTreeNode.setValue(subTagNode.getValue());
            subNode.getSunNodeList().addFirst(subTagXMLTreeNode);
          }
          treeNode.addSunNode(subNode);
        }
      } else {
        break;
      }
    }
  }
}
