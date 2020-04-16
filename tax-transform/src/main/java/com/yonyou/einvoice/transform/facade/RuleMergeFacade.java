package com.yonyou.einvoice.transform.facade;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 转换规则合并工具类
 *
 * @author liuqiangm
 */
@Slf4j
public class RuleMergeFacade {

  public static JSONObject getMergedRuleJSONObjectFromMasterRule(InputStream masterRuleInputStream) {
    try {
      String masterRuleStr = new String(ByteStreams.toByteArray(masterRuleInputStream), "UTF-8");
      return getMergedRuleJSONObjectFromMasterRule(masterRuleStr);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }

  /**
   * 根据主规则文件，查找子规则文件，并合并为一个单独的转换规则文件。
   * @param masterRuleStr
   * @return
   * @author liuqiangm
   */
  public static JSONObject getMergedRuleJSONObjectFromMasterRule(String masterRuleStr) {
    // 匹配规则字符串中的所有"*|*|*"格式的字符串
    Pattern pattern = Pattern.compile("(\")([^\"]*?\\|[^\"]*?\\|[^\"]*?)(\")");
    Matcher matcher = pattern.matcher(masterRuleStr);
    StringBuilder stringBuilder = new StringBuilder(masterRuleStr);
    List<InnerReplaceDimension> replaceDimensionList = new ArrayList<>();
    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      String parentDimension = matcher.group(2);
      JSONObject subRuleJSONObject = getSubJSONObjectFragmentOfParent(parentDimension);
      if(subRuleJSONObject.size() != 0) {
        InnerReplaceDimension replaceDimension = InnerReplaceDimension.builder().start(start)
            .end(end).content(subRuleJSONObject.toJSONString()).build();
        replaceDimensionList.add(replaceDimension);
      }
    }
    Collections.sort(replaceDimensionList);
    /**
     * 逆序替换。后面的替换不会对前面的字符串造成干扰
     * @author liuqiangm
     */
    for(InnerReplaceDimension replaceDimension : replaceDimensionList) {
      stringBuilder.replace(replaceDimension.getStart(), replaceDimension.getEnd(), replaceDimension.getContent());
    }
    return JSONObject.parseObject(stringBuilder.toString(), Feature.OrderedField);
  }

  /**
   * 根据父转换规则的坐标，转换为子JSON转换规则（嵌套在父转换规则之间）
   *
   * @param parentDimension
   * @return
   */
  public static JSONObject getSubJSONObjectFragmentOfParent(String parentDimension) {
    String[] splits = parentDimension.split("\\|");
    InputStream subRuleInputStream = RuleMergeFacade.class.getClassLoader().getResourceAsStream(splits[2]);
    if(subRuleInputStream == null) {
      return new JSONObject(Collections.emptyMap());
    }
    try {
      String subRuleStr = new String(ByteStreams.toByteArray(subRuleInputStream), "UTF-8");
      return getSubJSONObject(splits[0], splits[1], subRuleStr);
    } catch (IOException e) {
      log.error("", e);
      return new JSONObject(Collections.emptyMap());
    }
  }

  /**
   * 根据父转换规则的坐标，转换为可独立执行的子JSON转换规则。
   *
   * @param parentDimension
   * @return
   */
  public static JSONObject getSubJSONObjectIndividual(String parentDimension) {
    String[] splits = parentDimension.split("\\|");
    JSONObject subRuleFragment = getSubJSONObjectFragmentOfParent(parentDimension);
    JSONObject resultJSONObject = new JSONObject(new LinkedHashMap<>());
    JSONObject curJSONObject = new JSONObject(new LinkedHashMap<>());
    resultJSONObject.put("r->r", curJSONObject);
    String key1, key2;
    if(splits[1].endsWith(".")) {
      if(StringUtils.isEmpty(splits[0])) {
        key1 = splits[1].substring(0, splits[1].length() - 1);
        key2 = splits[1];
      }
      else {
        key1 = String.format("%s->%s", splits[0].substring(0, splits[0].length() - 1), splits[1].substring(0, splits[1].length() - 1));
        key2 = String.format("%s->%s", splits[0], splits[1]);
      }
      JSONObject tmpJSONObject = new JSONObject(new LinkedHashMap<>());
      curJSONObject.put(key1, tmpJSONObject);
      curJSONObject = tmpJSONObject;
      curJSONObject.put(key2, subRuleFragment);
      return resultJSONObject;
    }
    if(StringUtils.isEmpty(splits[0])) {
      key1 = splits[1];
    }
    else {
      key1 = String.format("%s->%s", splits[0], splits[1]);
    }
    curJSONObject.put(key1, subRuleFragment);
    return resultJSONObject;
  }

  private static JSONObject getSubJSONObject(String source, String target, String subRuleStr) {
    JSONObject sourceJSONObject = JSONObject.parseObject(subRuleStr, JSONObject.class,
        Feature.OrderedField);
    JSONObject resultJSONObject = new JSONObject(new LinkedHashMap<>());
    construct(sourceJSONObject.getJSONObject("r->r"), resultJSONObject, source, target);
    return resultJSONObject;
  }

  private static void construct(JSONObject source, JSONObject target, String parentSource,
      String parentTarget) {
    if (parentSource.endsWith(".")) {
      parentSource = parentSource.substring(0, parentSource.length() - 1);
    }
    if (parentTarget.endsWith(".")) {
      parentTarget = parentTarget.substring(0, parentTarget.length() - 1);
    }
    for (Map.Entry<String, Object> entry : ((JSONObject) source).entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      String[] triple = getTriple(key);
      StringBuilder keyBuilder = new StringBuilder();
      if (triple[2] != null) {
        keyBuilder.append(triple[2]).append("->");
      }
      if (triple[0] != null) {
        keyBuilder.append(parentSource)
            .append(".").append(triple[0]).append("->");
      }
      if (triple[1] != null) {
        keyBuilder.append(parentTarget)
            .append(".").append(triple[1]);
      }
      if (value instanceof JSONObject) {
        JSONObject subTarget = new JSONObject(new LinkedHashMap<>());
        construct((JSONObject) value, subTarget, parentSource, parentTarget);
        target.put(keyBuilder.toString(), subTarget);
        continue;
      }
      target.put(keyBuilder.toString(), value);
    }
    return;
  }

  private static String[] getTriple(String str) {
    String[] splits = str.split("->");
    if (splits.length != 1 && splits.length != 2) {
      log.info("转换规则的key存在语法问题，请检查:{}",str);
    }
    if (splits.length == 1 && str.startsWith("r.")) {
      return new String[]{null, str.substring(2), null};
    }
    if (splits.length == 2 && str.startsWith("r.")) {
      return new String[]{splits[0].substring(2), splits[1].substring(2), null};
    }
    if (splits.length == 2 && !str.startsWith("r.")) {
      return new String[]{null, splits[1].substring(2), splits[0]};
    }
    if (splits.length != 1 && splits.length != 2) {
      log.info("转换规则的key存在语法问题，请检查:{}",str);
    }
    return null;
  }

  @Setter
  @Getter
  @Builder
  public static class InnerReplaceDimension implements Comparable{
    int start;
    int end;
    String content;

    /**
     * 不会出现两个InnerReplaceDimension的start相同的情况，
     * 因此只比较start即可。
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
      InnerReplaceDimension other = (InnerReplaceDimension) o;
      return other.getStart() - this.getStart();
    }
  }
}
