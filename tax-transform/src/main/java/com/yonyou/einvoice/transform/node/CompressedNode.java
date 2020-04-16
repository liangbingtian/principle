package com.yonyou.einvoice.transform.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.LinkedList;
import java.util.List;

/**
 * 压缩树节点
 *
 * @author liuqiangm
 */
public class CompressedNode {

  private String sourcePath;

  private String targetPath;

  private Object value;

  private List<CompressedNode> subNodeList = new LinkedList<>();

  public CompressedNode() {
  }

  public JSONObject getTargetJSONObject() {
    return (JSONObject) getTargetJSON("", null);
  }

  public JSONArray getTargetJSONArray() {
    return (JSONArray) getTargetJSON("", null);
  }

  private JSON getTargetJSON(String parentPath, JSON parentNode) {
    if (parentNode == null) {
      parentNode = new JSONObject();
    }
    JSON treeNode = parentNode;
    String currPath = targetPath.substring(parentPath.length());
    // 对象数组中的对象
    if (".".equals(currPath)) {
      treeNode = new JSONObject();
      for (CompressedNode subCompressedNode : this.subNodeList) {
        subCompressedNode.getTargetJSON(this.getTargetPath(), treeNode);
      }
      ((JSONArray) parentNode).add(treeNode);
    } else if ("]".equals(currPath)) {
      // 常量数组中的常量
      ((JSONArray) parentNode).add(this.getValue());
    } else {
      String[] splits = currPath.split("\\.");
      JSON preNode = treeNode;
      JSON curNode = null;
      for (int i = 0; i < splits.length; i++) {
        String split = splits[i];
        if (!"".equals(split) && !"[".equals(split)) {
          // 当前元素为数组
          if (i != splits.length - 1 && "[".equals(splits[i + 1])) {
            curNode = new JSONArray();
          } else {
            // 当前元素非数组，可能是json对象，也可能是常量。
            curNode = new JSONObject();
          }
          // 如果当前元素是常量
          if (i == splits.length - 1 && this.getSubNodeList().size() == 0) {
            ((JSONObject) preNode).put(split, value);
          } else {
            // 如果当前元素是json对象
            ((JSONObject) preNode).put(split, curNode);
            preNode = curNode;
          }
        }
      }
      for (CompressedNode subCompressedNode : this.subNodeList) {
        subCompressedNode.getTargetJSON(this.getTargetPath(), preNode);
      }
    }
    return treeNode;
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

  public List<CompressedNode> getSubNodeList() {
    return subNodeList;
  }

  public void setSubNodeList(List<CompressedNode> subNodeList) {
    this.subNodeList = subNodeList;
  }
}
