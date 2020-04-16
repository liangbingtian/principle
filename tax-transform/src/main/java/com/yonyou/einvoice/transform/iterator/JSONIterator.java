package com.yonyou.einvoice.transform.iterator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.entity.Pair;
import com.yonyou.einvoice.transform.node.CompressedNode;
import com.yonyou.einvoice.transform.node.PatternNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * json遍历器，用于在遍历json的同时构建CompressedNode
 * @author liuqiangm
 */
public class JSONIterator {

  private Map<String, Object> objectContainerMap = new HashMap<>();

  // 根模板节点
  private PatternNode patternNode;

  // 当前模板节点
  private PatternNode curPatternNode;

  // 目标压缩树节点
  private CompressedNode compressedNode;

  // 生成中的当前压缩树节点
  private CompressedNode parentCompressedNode;

  private boolean reconstruct = false;

  private void addCompressedNodeToTree(CompressedNode cNode) {
    if (parentCompressedNode == null) {
      compressedNode = cNode;
    } else {
      parentCompressedNode.getSubNodeList().add(cNode);
    }
    parentCompressedNode = cNode;
  }

  public void iterator(Object object, String parentPath) {
    if (curPatternNode == null) {
      curPatternNode = patternNode;
    }
    PatternNode preCurPatternNode = curPatternNode;
    List<Pair<String, Object>> allSubNodes = new ArrayList<>();
    // 某些情况下，jsonobject里的对象会转换成map。此处兼容处理
    if(object instanceof Map) {
      object = new JSONObject((Map) object);
    }
    if(object instanceof List) {
      object = new JSONArray((List) object);
    }
    if(object instanceof JSON) {
      fillAllSubNode((JSON) object, parentPath, allSubNodes);
      addObjectContainerToMap(allSubNodes);
    }
    CompressedNode preCurCompressedNode = parentCompressedNode;
    if(isPatternSourcePathEmpty(curPatternNode)) {
      CompressedNode cNode = new CompressedNode();
      cNode.setTargetPath(curPatternNode.getTargetPath());
      cNode.setValue(curPatternNode.getValue());
      addCompressedNodeToTree(cNode);
      List<PatternNode> curSubPatternNodeList = curPatternNode.getSubNodeList();
      PatternNode tmpPatternNode = curPatternNode;
      for (PatternNode curSubPatternNode : curSubPatternNodeList) {
        curPatternNode = curSubPatternNode;
        iterator(object, parentPath);
        parentCompressedNode = cNode;
      }
      curPatternNode = tmpPatternNode;
    } else if (curPatternNode.getSourcePath().equals(parentPath)) {
      CompressedNode cNode = new CompressedNode();
      cNode.setSourcePath(curPatternNode.getSourcePath());
      cNode.setTargetPath(curPatternNode.getTargetPath());
      if(!(object instanceof JSON)) {
        cNode.setValue(object);
      }
      addCompressedNodeToTree(cNode);
      List<PatternNode> curSubPatternNodeList = curPatternNode.getSubNodeList();
      Set<PatternNode> noMatchPatternNodeSet = new HashSet<>(curSubPatternNodeList);
      for (PatternNode subPatternNode : curSubPatternNodeList) {
        Iterator<Pair<String, Object>> subNodeIterator = allSubNodes.iterator();
        while (subNodeIterator.hasNext()) {
          Pair<String, Object> subNodePair = subNodeIterator.next();
          if (!isPatternSourcePathEmpty(subPatternNode) && subPatternNode.getSourcePath()
              .startsWith(subNodePair.getKey())) {
            curPatternNode = subPatternNode;
            this.iterator(subNodePair.getValue(), subNodePair.getKey());
            noMatchPatternNodeSet.remove(subPatternNode);
          } else if (isPatternSourcePathEmpty(subPatternNode)) {
            curPatternNode = subPatternNode;
            this.iterator(subNodePair.getValue(), subNodePair.getKey());
            noMatchPatternNodeSet.remove(subPatternNode);
            break;
          }
        }
      }
      // 对于主子 -> 主子孙的转换，获取到节点对象容器，并预赋值。
      for (PatternNode abstractPatternNode : noMatchPatternNodeSet) {
        noMatchConstructTree(abstractPatternNode);
      }
    }
    // 匹配了部分前缀节点
    else if (curPatternNode.getSourcePath().startsWith(parentPath)) {
      Iterator<Pair<String, Object>> subNodeIterator = allSubNodes.iterator();
      while (subNodeIterator.hasNext()) {
        Pair<String, Object> subNodePair = subNodeIterator.next();
        if(curPatternNode.getSourcePath().startsWith(subNodePair.getKey())) {
          this.iterator(subNodePair.getValue(), subNodePair.getKey());
        }
      }
    } else {
      noMatchConstructTree(curPatternNode);
    }
    parentCompressedNode = preCurCompressedNode;
    curPatternNode = preCurPatternNode;
    removeObjectContainerOfMap(allSubNodes);
  }

  /**
   * 检查规则节点的源节点是否为空或非r开头节点
   * 非r开头节点用于对不同的key作区分
   * @param patternNode
   * @return
   */
  private boolean isPatternSourcePathEmpty(PatternNode patternNode) {
    return patternNode.getSourcePath() == null || !patternNode.getSourcePath().startsWith("r");
  }

  private void noMatchConstructTree(PatternNode patternNode) {
    CompressedNode ccNode = new CompressedNode();
    ccNode.setTargetPath(patternNode.getTargetPath());
    ccNode.setSourcePath(patternNode.getSourcePath());
    ccNode.setValue(objectContainerMap.get(patternNode.getSourcePath()));
    parentCompressedNode.getSubNodeList().add(ccNode);
  }

  private void addObjectContainerToMap(List<Pair<String, Object>> subNodeList) {
    Iterator<Pair<String, Object>> iterator = subNodeList.iterator();
    while (iterator.hasNext()) {
      Pair<String, Object> pair = iterator.next();
      if(!(pair.getValue() instanceof JSON)) {
        objectContainerMap.put(pair.getKey(), pair.getValue());
      }
    }
  }


  private void removeObjectContainerOfMap(List<Pair<String, Object>> subNodeList) {
    Iterator<Pair<String, Object>> iterator = subNodeList.iterator();
    while (iterator.hasNext()) {
      objectContainerMap.remove(iterator.next().getKey());
    }
  }

  private void fillAllSubNode(JSON json, String parentPath, List<Pair<String, Object>> allSubNodes) {
    if(json instanceof JSONObject) {
      Set<Entry<String, Object>> entrySet = ((JSONObject) json).entrySet();
      Iterator<Entry<String, Object>> entryIterator = entrySet.iterator();
      while (entryIterator.hasNext()) {
        Entry<String, Object> entry = entryIterator.next();
        Object value = entry.getValue();
        // 兼容从jackson转成的fastjson中的jsonobject、jsonarray对象
        if(value instanceof Map) {
          value = new JSONObject((Map) value);
        }
        if(value instanceof List) {
          value = new JSONArray((List) value);
        }
        if(value instanceof JSONObject) {
          allSubNodes.add(new Pair(parentPath + (parentPath.endsWith(".")? "": ".") + entry.getKey(), value));
        }
        else if(value instanceof JSONArray) {
          allSubNodes.add(new Pair(parentPath + (parentPath.endsWith(".")? "": ".") + entry.getKey() + ".[", value));
        }
        else {
          allSubNodes.add(new Pair(parentPath + (parentPath.endsWith(".")? "": ".") + entry.getKey(), value));
        }
      }
    }
    else if(json instanceof JSONArray) {
      Iterator<Object> iterator = ((JSONArray) json).iterator();
      while (iterator.hasNext()) {
        Object object = iterator.next();
        if(object instanceof Map) {
          object = new JSONObject((Map) object);
        }
        if(object instanceof List) {
          object = new JSONArray((List) object);
        }
        if(object instanceof JSONObject) {
          allSubNodes.add(new Pair(parentPath + (parentPath.endsWith(".")? "": "."), object));
        }
        else if(object instanceof JSONArray) {
          allSubNodes.add(new Pair(parentPath + (parentPath.endsWith(".")? "": ".") + "[", object));
        }
        else {
          allSubNodes.add(new Pair(parentPath + "]", object));
        }
      }
    }
  }

  public PatternNode getPatternNode() {
    return patternNode;
  }

  public void setPatternNode(PatternNode patternNode) {
    this.patternNode = patternNode;
  }

  public PatternNode getCurPatternNode() {
    return curPatternNode;
  }

  public void setCurPatternNode(PatternNode curPatternNode) {
    this.curPatternNode = curPatternNode;
  }

  public CompressedNode getCompressedNode() {
    if (!reconstruct && compressedNode != null) {
      reconstruct(compressedNode);
    }
    reconstruct = true;
    return compressedNode;
  }

  public void setCompressedNode(CompressedNode compressedNode) {
    this.compressedNode = compressedNode;
  }

  public CompressedNode getParentCompressedNode() {
    return parentCompressedNode;
  }

  public void setParentCompressedNode(CompressedNode parentCompressedNode) {
    this.parentCompressedNode = parentCompressedNode;
  }

  private void reconstruct(CompressedNode cNode) {
    for (CompressedNode subCompressNode : cNode.getSubNodeList()) {
      // 解决原为[]，目标变为[null]的问题
      if(subCompressNode.getTargetPath().endsWith(".[.") && subCompressNode.getSubNodeList().size() == 0) {
        cNode.getSubNodeList().clear();
        break;
      }
      reconstruct(subCompressNode);
    }
  }
}
