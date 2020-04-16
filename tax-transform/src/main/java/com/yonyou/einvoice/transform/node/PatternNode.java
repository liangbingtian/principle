package com.yonyou.einvoice.transform.node;

import com.alibaba.fastjson.JSONObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * 模式匹配规则节点
 * @author liuqiangm
 */
public class PatternNode {

  private String sourcePath;

  private String targetPath;

  private Object value;

  private List<PatternNode> subNodeList = new LinkedList<>();

  public PatternNode() {
  }

  public static PatternNode getPatternNodeFromJSONObject(JSONObject jsonObject) {
    PatternNode patternNode = new PatternNode();
    if (jsonObject.size() == 1) {
      for (Entry<String, Object> entry : jsonObject.entrySet()) {
        fillPatternNode(patternNode, entry);
      }
    } else if (jsonObject.size() != 0) {
      throw new RuntimeException("jsonObject状态不正确");
    }
    return patternNode;
  }

  private static void fillPatternNode(PatternNode patternNode, Entry<String, Object> entry) {
    String key = entry.getKey();
    Object value = entry.getValue();
    String[] splits = key.split("->");
    if (splits.length == 2) {
      patternNode.setSourcePath(splits[0]);
      patternNode.setTargetPath(splits[1]);
      if (value instanceof JSONObject) {
        if (((JSONObject) value).size() == 1) {
          patternNode.getSubNodeList().add(getPatternNodeFromJSONObject((JSONObject) value));
        } else if (((JSONObject) value).size() != 0) {
          patternNode.getSubNodeList().addAll(getPatternNodeListFromJSONObject((JSONObject) value));
        }
      }
    } else {
      patternNode.setTargetPath(key);
      if (value instanceof JSONObject) {
        patternNode.getSubNodeList().addAll(getPatternNodeListFromJSONObject((JSONObject) value));
      } else {
        patternNode.setValue(value);
      }
    }
  }

  private static List<PatternNode> getPatternNodeListFromJSONObject(JSONObject jsonObject) {
    List<PatternNode> resultList = new LinkedList<PatternNode>();
    for (Entry<String, Object> entry : jsonObject.entrySet()) {
      PatternNode patternNode = new PatternNode();
      fillPatternNode(patternNode, entry);
      resultList.add(patternNode);
    }
    return resultList;
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }

  public String getTargetPath() {
    return targetPath;
  }

  public void setTargetPath(String targetPath) {
    this.targetPath = targetPath;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public List<PatternNode> getSubNodeList() {
    return subNodeList;
  }

  public void setSubNodeList(List<PatternNode> subNodeList) {
    this.subNodeList = subNodeList;
  }
}
