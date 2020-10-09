package com.sopristec.extractor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class NodeBuilder {

    public abstract Element buildInstrumentationElement(String metricPrefixValue, Document doc);

    public Element buildExtensionElement(String extensionName, Document doc) {
        Element mainRootElement = doc.createElement("extension");
        mainRootElement.setAttribute("xmlns", "https://newrelic.com/docs/java/xsd/v1.0");
        mainRootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        mainRootElement.setAttribute("xsi:schemaLocation", "newrelic-extension extension.xsd ");
        mainRootElement.setAttribute("name", extensionName + "Extension");
        mainRootElement.setAttribute("version", "1.0");
        doc.appendChild(mainRootElement);
        return mainRootElement;
    };

    public abstract Element buildPointcutElement(boolean transactionStartPoint, Document doc);
}
