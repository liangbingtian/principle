import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.transform.facade.JSONTransformFacade;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liangbingtian on 2020/4/17 11:15 上午
 */
public class JSONTest {

  /**
   * json数组转索引对象
   */
  @Test
  public void testJSONObjectWithIndex() {
    String jsonString = "{\"a\":{\"b\":[\"c\",\"d\"],\"e\":[[[\"g\",\"h\"]],[[\"i\",\"j\"]]]}}";
    JSONObject jsonObject = JSONObject.parseObject(jsonString);
    JSONObject targetJsonObject = JSONTransformFacade.getJSONObjectWithIndex(jsonObject);
    String targetJsonObjectStr = "{\"a\":{\"b\":{\"_0\":\"c\",\"_1\":\"d\"},\"e\":{\"_0\":{\"_0\":{\"_0\":\"g\",\"_1\":\"h\"}},\"_1\":{\"_0\":{\"_0\":\"i\",\"_1\":\"j\"}}}}}";
    Assert.assertEquals(targetJsonObject.toJSONString(), targetJsonObjectStr);
  }

  @Test
  public void testonebyone() {
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("source1.json");
    InputStream ruleStream = this.getClass().getClassLoader().getResourceAsStream("source1_onebyone.json");
    JSONObject targetJsonObject = JSONTransformFacade.getJSONObjectFromSourceAndPattern(inputStream, ruleStream);
    System.out.println(targetJsonObject);
    String targetJsonObjectStr = "{\"a1\":{\"d1\":[{\"e1\":{\"f1\":1}},{\"e1\":{\"f1\":2}}],\"c1\":[1,2],\"b1\":123}}";
    Assert.assertEquals(targetJsonObject.toJSONString(), targetJsonObjectStr);
  }

  @Test
  public void testJSON() throws IOException {
    JSONObject jsonObject
        =  new JSONObject(JSONObject
        .parseObject(this.getClass().getClassLoader().getResourceAsStream("source1.json")
            , LinkedHashMap.class));
    JSONObject jsonObject1 = JSON.
        parseObject(
            JSON.toJSONString(jsonObject).replaceAll("e", "e1"));
    System.out.println(jsonObject1);
  }

}
