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

    private final Document doc;
    private final Element instrumentationElement;
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

    public Element appendAndGetPointcutElement(boolean transactionStartPoint){
        pointcutElement = doc.createElement("pointcut");
        pointcutElement.setAttribute("transactionStartPoint", transactionStartPoint ? "true" : "false");
        return (Element) instrumentationElement.appendChild(pointcutElement);
    }

    public void appendToPointcutNode(Node node) {
        pointcutElement.appendChild(node);
    }

    public Node getClassNode(String className){
        Element classNameElement = doc.createElement("className");
        classNameElement.appendChild(doc.createTextNode(className));
        return classNameElement;
    }

    public Node getMethodNode(String methodName, ArrayList<String> parameterList) {
        // Create and append method and name elements
        Element methodElement = doc.createElement("method");
        Element methodNameElement = doc.createElement("name");
        methodNameElement.appendChild(doc.createTextNode(methodName));
        methodElement.appendChild(methodNameElement);

        // Create and append parameters and its type elements
        Element parametersElement = doc.createElement("parameters");
        parameterList.forEach(parameter -> {
            Element typeElement = doc.createElement("type");
            typeElement.appendChild(doc.createTextNode(parameter));
            parametersElement.appendChild(typeElement);
        });
        methodElement.appendChild(parametersElement);

        return methodElement;
    }

    public String encode(){
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        assert transformer != null;
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
