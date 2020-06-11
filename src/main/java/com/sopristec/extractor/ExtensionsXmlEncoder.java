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
    private Element instrumentationElement = null;
    private Element pointcutElement = null;

    public ExtensionsXmlEncoder(String metricPrefixValue) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        Element mainRootElement = doc.createElement("extension");
        mainRootElement.setAttribute("xmlns", "https://newrelic.com/docs/java/xsd/v1.0");
        mainRootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        mainRootElement.setAttribute("xsi:schemaLocation", "newrelic-extension extension.xsd ");
        mainRootElement.setAttribute("name", "customExtension");
        doc.appendChild(mainRootElement);

        instrumentationElement = doc.createElement("instrumentation");
        instrumentationElement.setAttribute("metricPrefix", metricPrefixValue);
        mainRootElement.appendChild(instrumentationElement);
    }

    public Element getPointcutElement(boolean transactionStartPoint){
        pointcutElement = doc.createElement("pointcut");
        pointcutElement.setAttribute("transactionStartPoint", transactionStartPoint ? "true" : "false");
        return (Element) instrumentationElement.appendChild(pointcutElement);
    }

    public Node appendToPointcutNode(Node node)
    {
        return pointcutElement.appendChild(node);
    }

    public Node getClassNode(String name){
        Element classNameElement = doc.createElement("className");
        classNameElement.appendChild(doc.createTextNode(name));
        pointcutElement.appendChild(classNameElement);
        return classNameElement;
    }

    public Node getMethodNode(String name, ArrayList<String> parameterTypes){
        // 1. Create parent method node
        Element methodNode = doc.createElement("method");
        pointcutElement.appendChild(methodNode);

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
        pointcutElement.appendChild(methodNode);
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
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return writer.getBuffer().toString();//.replaceAll("\n|\r", "");
    }
}
