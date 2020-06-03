package com.sopristec.extractor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.StringWriter;
import java.util.ArrayList;

public class ExtensionsXmlEncoder {

    private Document doc = null;

    public ExtensionsXmlEncoder(String metricPrefixValue) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        Element mainRootElement = doc.createElement("instrumentation");
        mainRootElement.setAttribute("metricPrefix", metricPrefixValue);
        doc.appendChild(mainRootElement);
    }

    public Node getPointcutNode(boolean transactionStartPoint){
        Element node = doc.createElement("pointcut");
        node.setAttribute("transactionStartPoint", transactionStartPoint ? "true" : "false");
        return node;
    }

    public Node getClassNode(Node pointcutNode, String name){
        Element classNameNode = doc.createElement("className");
        classNameNode.appendChild(doc.createTextNode(name));
        pointcutNode.appendChild(classNameNode);
        return classNameNode;
    }

    public Node getMethodNode(Node pointcutNode, String name, ArrayList<String> parameterTypes){
        // 1. Create parent method node
        Element methodNode = doc.createElement("method");
        pointcutNode.appendChild(methodNode);

        // 2. Create method name node
        Element nameNode = doc.createElement("name");
        nameNode.appendChild(doc.createTextNode(name));
        methodNode.appendChild(nameNode);

        // 3. Create parameters node and it's children
        Element paramsNode = doc.createElement("parameters");
        parameterTypes.forEach( s ->{
                Element typeNode = doc.createElement("type");
                typeNode.appendChild(doc.createTextNode(s));
        });
        methodNode.appendChild(paramsNode);

        // 4. Finally append to pointcut node
        pointcutNode.appendChild(methodNode);
        return methodNode;
    }

    public String encode(){
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return writer.getBuffer().toString();//.replaceAll("\n|\r", "");
    }
}
