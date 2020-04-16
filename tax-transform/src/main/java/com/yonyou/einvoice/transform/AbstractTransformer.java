package com.yonyou.einvoice.transform;

import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.facade.EntityTransformFacade;
import com.yonyou.einvoice.transform.facade.JSONTransformFacade;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象的转换类。在各个facade类的基础上，进行了进一步封装。主要提升如下：
 * 1. 子类中实现getConfigPackageName接口，指定config规则所存放的resource目录下的路径。例如：transform/ocr。
 * 2. 约定优于配置。对于json数据转Java对象，如：Invoice类。
 *  则从config规则存放目录下，查找invoice.json文件。规则文件名为实体类的全小写 + ".json"后缀。
 *  基于该考虑，当系统中需要增加新支持的转换类型时，可以直接在config配置文件目录下增加转换规则，而无需修改代码。
 * 3. 对于对象 -- 对象转换。则默认的规则文件命名要根据转换前后的对象类型判定。
 *  例如，对于AirVO类型对象转换为SummaryVO类型对象，则相应的转换规则文件命名为：airvo_summaryvo.json。
 *  转换前后类型全小写，中间以下划线分割。
 *
 * @author liuqiangm
 * @date 2019年8月1日16:01:05
 */

@Slf4j
public abstract class AbstractTransformer {

  private Map<String, JSONObject> transformConfigMap = new ConcurrentHashMap<>();

  /**
   * 将源json字符串转换为目标Java对象T。
   * 若目标对象的class为AirVO.class，则相应的转换规则配置文件为airvo.json
   * @param jsonString
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> T getTargetObjectFromJSONStr(String jsonString, Class<T> clazz) {
    JSONObject jsonObject = JSONObject.parseObject(jsonString);
    return getTargetObjectFromJSONObject(jsonObject, clazz);
  }

  /**
   * 根据源java对象列表和目标对象类型，转换为目标对象列表
   * @param sourceObjectList
   * @param targetClazz
   * @param <T>
   * @return
   */
  public <T, R> List<T> getTargetObjectListFromSourceJavaObjectList(List<R> sourceObjectList, Class<? extends T> targetClazz) {
    return Optional.ofNullable(sourceObjectList).orElse(Collections.emptyList())
        .parallelStream()
        .map(sourceObject -> getTargetObjectFromSourceJavaObject(sourceObject, targetClazz))
        .collect(Collectors.toList());
  }

  /**
   * 根据转换规则ruleFilePath，将源对象列表中的对象转换为目标targetClazz对象列表
   *
   * @param sourceObjectList
   * @param ruleFileName
   * @param targetClazz
   * @param <T>
   * @return
   */
  public <T, R> List<T> getTargetObjectListFromSourceJavaObjectList(List<R> sourceObjectList,
      String ruleFileName, Class<? extends T> targetClazz) {
    JSONObject ruleJsonObject = getTransformRuleJSONObject(ruleFileName);
    return Optional.ofNullable(sourceObjectList)
        .orElse(Collections.emptyList())
        .parallelStream()
        .map(sourceObject -> (T) EntityTransformFacade
            .getTargetObjectFromSourceJavaObject(sourceObject, ruleJsonObject, targetClazz))
        .collect(Collectors.toList());
  }

  public <T> T getTargetObjectFromInputStream(InputStream inputStream, Class<T> clazz) throws IOException {
    JSONObject jsonObject = JSONObject.parseObject(inputStream, JSONObject.class);
    return getTargetObjectFromJSONObject(jsonObject, clazz);
  }

  public <T> T getTargetObjectFromJSONObject(JSONObject jsonObject, Class<T> clazz) {
    JSONObject ruleJSONObject = getTransformRuleJSONObject(clazz.getSimpleName());
    return EntityTransformFacade.getTargetObjectFromSourceJavaObject(jsonObject, ruleJSONObject, clazz);
  }

  public JSONObject getTargetJSONObjectFromJSONStr(String jsonString, Class clazz) {
    JSONObject jsonObject = JSONObject.parseObject(jsonString);
    return getTargetJSONObjectFromJSONObject(jsonObject, clazz);
  }

  public JSONObject getTargetJSONObjectFromJSONObject(JSONObject jsonObject, Class clazz) {
    JSONObject ruleJSONObject = getTransformRuleJSONObject(clazz.getSimpleName());
    return JSONTransformFacade.getJSONObjectFromSourceAndPattern(jsonObject, ruleJSONObject);
  }

  public JSONObject getTargetJSONObjectFromJSONObject(JSONObject jsonObject, String ruleFileName) {
    JSONObject ruleJSONObject = getTransformRuleJSONObject(ruleFileName);
    return JSONTransformFacade.getJSONObjectFromSourceAndPattern(jsonObject, ruleJSONObject);
  }

  /**
   * 根据源Java对象和目标对象class，获取目标对象。
   * 依然采取约定优于配置的方式
   * 例如，若对象从AirVO转换为SummaryVO，则默认配置文件名称为:airvo_summaryvo.json。
   * @param sourceObject
   * @param targetClazz
   * @param <T>
   * @return
   */
  public <T> T getTargetObjectFromSourceJavaObject(Object sourceObject, Class<T> targetClazz) {
    Class sourceClazz = sourceObject.getClass();
    return getTargetObjectFromSourceJavaObject(sourceObject, sourceClazz, targetClazz);
  }

  /**
   * 用于对Java中的运行时绑定进行兼容。
   * 例如，某个Java对象是Entity类，但是该类为Interface接口的实现。若直接获取Interface引用的Entity对象，在此处调用时，容易产生误区。
   * 虽然表面上对象被Interface接口引用，但实际上会查找的转换文件为：entity_***.json。
   * 为了提升代码的可读性，在涉及到接口或抽象类引用时，可直接将具体的源对象Class类传入参数当中
   * @param sourceObject
   * @param sourceClazz
   * @param targetClazz
   * @param <T>
   * @return
   */
  public <T> T getTargetObjectFromSourceJavaObject(Object sourceObject, Class sourceClazz, Class<T> targetClazz) {
    if (Objects.equals(sourceClazz, targetClazz)) {
      return (T) sourceObject;
    }
    JSONObject ruleJsonObject = getTransformRuleJSONObject(String.format("%s_%s", sourceClazz.getSimpleName(), targetClazz.getSimpleName()));
    if (ruleJsonObject == null) {
      log.info("找不到配置文件");
    }
    return EntityTransformFacade.getTargetObjectFromSourceJavaObject(sourceObject, ruleJsonObject, targetClazz);
  }

  /**
   * 根据转换规则文件ruleFileName，将sourceObject转换为目标类型targetClazz的对象
   *
   * @param sourceObject
   * @param ruleFileName
   * @param targetClazz
   * @param <T>
   * @return
   */
  public <T> T getTargetObjectFromSourceJavaObject(Object sourceObject, String ruleFileName,
      Class<T> targetClazz) {
    JSONObject ruleJsonObject = getTransformRuleJSONObject(ruleFileName);
    return EntityTransformFacade
        .getTargetObjectFromSourceJavaObject(sourceObject, ruleJsonObject, targetClazz);
  }


  /**
   * 用于根据规则名称，结合规则目录，获取相应的配置文件。
   * 使用transformConfigMap在内存中进行缓存，减少磁盘读取，提升性能。
   * @param ruleFileName
   * @return
   */
  private JSONObject getTransformRuleJSONObject(String ruleFileName) {
    String ruleDirName = getConfigPackageName();
    /**
     * 使用concurrentHashMap，不会产生hashmap扩容可能产生的死循环问题。
     * 另外，此处即便多次实例化同一个配置文件也没有问题，无需严格遵守单例模式，未采用单例的双重锁机制。
     */
    if(!transformConfigMap.containsKey(ruleFileName)) {
      // 转换规则存放目录以'.'或'/'分割。首先将'.'替换为'/'
      while (ruleDirName.indexOf('.') != -1) {
        ruleDirName = ruleDirName.replace('.', '/');
      }
      // 转换目录以'/'开始，以'/'结束
      if(!ruleDirName.startsWith("/")) {
        ruleDirName = '/' + ruleDirName;
      }
      if(!ruleDirName.endsWith("/")) {
        ruleDirName = ruleDirName + "/";
      }
      /**
       * 先匹配全小写文件。如果全小写文件名称不存在，则查找全大写文件。
       * 如果都不存在，则报错
       * @author liuqiangm
       */
      String rulePath = ruleDirName + ruleFileName.toLowerCase() + ".json";
      try (InputStream inputStream = this.getClass().getResourceAsStream(rulePath)) {
        JSONObject ruleJsonObject = JSONObject.parseObject(inputStream, JSONObject.class);
        transformConfigMap.put(ruleFileName, ruleJsonObject);
      }
      catch (Exception e) {
        rulePath = ruleDirName + ruleFileName + ".json";
        try (InputStream inputStream1 = this.getClass().getResourceAsStream(rulePath)) {
          JSONObject ruleJsonObject = JSONObject.parseObject(inputStream1, JSONObject.class);
          transformConfigMap.put(ruleFileName, ruleJsonObject);
        } catch (Exception e1) {
          log.error("转换规则文件不存在，规则路径:{}", rulePath, e1);
        }
      }
    }
    return transformConfigMap.get(ruleFileName);
  }

  /**
   * 模板方法，由子类指定配置文件的存放包名。
   * @return
   */
  protected abstract String getConfigPackageName();
}
