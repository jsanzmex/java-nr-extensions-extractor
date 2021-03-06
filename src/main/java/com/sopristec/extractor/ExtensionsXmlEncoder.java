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
    private final NodeBuilder nodeBuilder;
    private Element pointcutElement;

    // TODO: Investigate if unescaping &gt; & &lt; would make NewRelic agents crash

    public ExtensionsXmlEncoder(String metricPrefixValue, String extensionName, String newRelicAgentVersion) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        nodeBuilder = NodeBuilderFactory.createNodeBuilder(newRelicAgentVersion);
        Element mainRootElement = nodeBuilder.buildExtensionElement(extensionName, doc);
        instrumentationElement = nodeBuilder.buildInstrumentationElement(metricPrefixValue, doc);
        mainRootElement.appendChild(instrumentationElement);
    }

    public Element appendAndGetPointcutElement(boolean transactionStartPoint){
        pointcutElement = nodeBuilder.buildPointcutElement(transactionStartPoint, doc);
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
        if(parameterList.size() > 0){
            methodElement.appendChild(parametersElement);
        }

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
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        // Uncomment replaceAll to remove space and new-line characters in your final XML.
        return writer.getBuffer().toString();//.replaceAll("\n|\r", "");
    }
}
