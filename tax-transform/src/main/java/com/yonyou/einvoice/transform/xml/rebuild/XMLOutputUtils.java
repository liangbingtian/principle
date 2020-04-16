package com.yonyou.einvoice.transform.xml.rebuild;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 用于输出xml内容到输出流或字符串
 * @author liuqiangm
 */
public class XMLOutputUtils {

  private InputStream xmlInputStream;

  private String jsonRuleString;

  public XMLOutputUtils setSourceXMLInputStream(InputStream inputStream) throws IOException {
    this.xmlInputStream = inputStream;
    return this;
  }

  public XMLOutputUtils setConvertRuleInputStream(InputStream inputStream) throws IOException {
    this.jsonRuleString = getStringFromInputStream(inputStream);
    return this;
  }

  private String getStringFromInputStream(InputStream inputStream) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = inputStream.read(buffer)) != -1) {
      result.write(buffer, 0, length);
    }
    return result.toString("UTF-8");
  }

  public XMLOutputUtils setSourceXMLString(String sourceXMLString) throws IOException {
    return setSourceXMLInputStream(
        new ByteArrayInputStream(sourceXMLString.getBytes(Charset.forName("UTF-8"))));
  }

  public XMLOutputUtils setConvertRuleString(String jsonRuleString) throws IOException {
    this.jsonRuleString = jsonRuleString;
    return this;
  }

  /**
   * 根据XMLTreeNode创建xml字符串
   * @param treeNode
   * @return
   */
  public static String createXMLString(XMLTreeNode treeNode) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    writeXMLToOutputStream(treeNode, bos);
    return bos.toString();
  }

  /**
   * 直接将节点输出到outputStream，可以避免创建大字符串造成的内存开销
   */
  public static void writeXMLToOutputStream(XMLTreeNode treeNode, OutputStream outputStream) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();
      document.setXmlStandalone(true);
      xmlIter(document, treeNode, null);
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(
          OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource domSource = new DOMSource(document);
      transformer.transform(domSource, new StreamResult(outputStream));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void xmlIter(Document document, XMLTreeNode treeNode, Element element) {
    Element element1 = document.createElement(treeNode.getKey());
    if (treeNode.getSunNodeList().size() == 0) {
      if (treeNode.getValue() == null) {
        element1.setTextContent("");
      } else {
        element1.setTextContent(treeNode.getValue());
      }
    }
    if (element == null) {
      document.appendChild(element1);
    } else {
      element.appendChild(element1);
    }
    for (XMLTreeNode subXMLTreeNode : treeNode.getSunNodeList()) {
      xmlIter(document, subXMLTreeNode, element1);
    }
  }
}
