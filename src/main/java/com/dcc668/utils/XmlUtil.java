package com.dcc668.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {

    /**
     * xml转Map
     *
     * @param xml
     * @return
     */
    public static Map<String, String> xmltoMap(String xml) {
        Map<String, String> map = new HashMap<>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            List<Element> list = rootElt.elements();//获取根节点下所有节点
//            list.forEach(entry -> map.put(entry.getName(), entry.getText()));
            for(Element ele:list){
                map.put(ele.getName(),ele.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Map转Xml
     *
     * @param paramMap
     * @return
     */
    public static String maptoXml(Map<String, String> paramMap) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("xml");
//        paramMap.entrySet().forEach(entry -> root.addElement(entry.getKey()).addCDATA(entry.getValue()));
        for(Map.Entry<String,String> entry:paramMap.entrySet()){
            root.addElement(entry.getKey()).addCDATA(entry.getValue());
        }
        return root.asXML();
    }
}
