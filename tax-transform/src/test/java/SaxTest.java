import com.yonyou.einvoice.saxtest.utils.SaxUtils;
import java.io.File;
import java.io.IOException;
import org.dom4j.DocumentException;
import org.junit.Test;

/**
 * Created by liangbingtian on 2020/5/7 7:51 下午
 */
public class SaxTest {

  @Test
  public void readDocumentTest() throws IOException, DocumentException {
    SaxUtils.read(new File("/Users/liangbingtian/Desktop/测试/Untitled2.xml"));


  }

}
