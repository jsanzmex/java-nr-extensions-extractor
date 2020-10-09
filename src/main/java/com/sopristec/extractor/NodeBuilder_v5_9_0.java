package com.sopristec.extractor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NodeBuilder_v5_9_0 extends NodeBuilder {

    @Override
    public Element buildInstrumentationElement(String metricPrefixValue, Document doc) {
        final Element instrumentationElement;
        instrumentationElement = doc.createElement("instrumentation");
        instrumentationElement.setAttribute("metricPrefix", metricPrefixValue);
        return instrumentationElement;
    }

    @Override
    public Element buildPointcutElement(boolean transactionStartPoint, Document doc) {
        Element pointcutElement = doc.createElement("pointcut");
        pointcutElement.setAttribute("transactionStartPoint", transactionStartPoint ? "true" : "false");
        return pointcutElement;
    }

}
