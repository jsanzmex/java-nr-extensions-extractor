package com.sopristec.extractor;

public class NodeBuilderFactory {

    public static NodeBuilder createNodeBuilder(String newRelicAgentVersion) {
        switch (newRelicAgentVersion) {
            case "5.9.0":
                return new NodeBuilder_v5_9_0();
            case "4.3.0":
                return new NodeBuilder_v4_3_0();
        }
        return new NodeBuilder_v5_9_0();
    }

}
