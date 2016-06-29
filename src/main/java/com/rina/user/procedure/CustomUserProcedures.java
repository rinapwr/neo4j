package com.rina.user.procedure;

import com.rina.common.result.NodeResult;
import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import com.rina.common.result.StringResult;

import java.util.*;

import java.util.stream.Stream;

/**
 * Created by rinacao on 6/27/16.
 */
public class CustomUserProcedures {
    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;

    /**
     * Custom User Procedure: Given a node's label and a node property, return the node
     * @param label
     * @param key
     * @param value
     * @return
     */
    @Procedure("get.nodes.by.property")
    public Stream<NodeResult> searchByProperty(@Name("label") String label, @Name("key") String key, @Name("value") String value)
    {
        log.info("in get.nodes.by.property");
        Label lab = Label.label(label);

        ResourceIterator<Node> nodes  = db.findNodes(lab, key, value);
        return Stream.of(new NodeResult(nodes.next()));
    }

    /**
     * Custom User Procedure: Given a node label, return all property keys
     * @param label
     * @return
     */
    @Procedure("get.keys.by.label")
    public Stream<StringResult> getKeysByLabel(@Name("label") String label)
    {
        log.info("in get.keys.by.label");

        Label lab = Label.label(label);
        ResourceIterator<Node> nodes  = db.findNodes(lab);

        Set<String> propertySet = new HashSet<>();
        nodes.forEachRemaining(node1 -> {
            node1.getPropertyKeys().forEach(prop-> propertySet.add(prop));
        });

        return Stream.of(new StringResult(propertySet.toString()));
    }
}
