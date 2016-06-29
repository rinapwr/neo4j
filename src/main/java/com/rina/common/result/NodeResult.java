package com.rina.common.result;

import org.neo4j.graphdb.Node;

/**
 * Created by rinacao on 6/28/16.
 */
public class NodeResult {
    public final Node node;

    public NodeResult(Node node) {
        this.node = node;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && node.equals(((NodeResult) o).node);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }
}
