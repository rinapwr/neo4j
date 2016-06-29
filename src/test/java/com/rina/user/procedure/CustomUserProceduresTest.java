package com.rina.user.procedure;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.server.rest.JaxRsResponse;
import org.neo4j.server.rest.RestRequest;
import org.neo4j.shell.util.json.JSONObject;

import static org.junit.Assert.assertEquals;

/**
 * Created by rinacao on 6/28/16.
 */
public class CustomUserProceduresTest {

    private RestRequest REST_REQUEST;

    @Rule
    public Neo4jRule server = new Neo4jRule().withProcedure(CustomUserProcedures.class);

    @Before
    public void setUp() throws Exception {
        System.err.println(server.httpURI());
        REST_REQUEST = new RestRequest(server.httpURI());
    }

    /**
     * Test Cypher query (user procedure) call from bolt driver
     */
    @Test
    public void testCustomUserProceduresFromBoltDriver () {
        // In a try-block, to make sure we close the driver after the test
        try( Driver driver = GraphDatabase.driver(server.boltURI(),
                Config.build().withEncryptionLevel( Config.EncryptionLevel.NONE ).toConfig() ) ) {

            Session session = driver.session();
            session.run("CREATE (a:Person {name:'Arthur', title:'King'})");

            // Call custom user procedure get.keys.by.label
            StatementResult result = session.run("CALL get.keys.by.label('Person')");
            Record record = result.single();

            Assert.assertEquals("\"[name, title]\"", record.get("value").toString());

            session.close();
            driver.close();
        }
    }

    /**
     * This is the Legacy Cypher HTTP Endpoint
     * http://neo4j.com/docs/rest-docs/current/#rest-api-use-parameters
     * @throws Exception
     */
    @Test
    public void testCustomUserProceduresFromHttpEndpoint () throws Exception {

        JSONObject json = new JSONObject();
        json.put("query", "CREATE (a:Person {name:'Bob', age: 16}) RETURN a");

        JaxRsResponse response = REST_REQUEST.post("db/data/cypher", json.toString());
        assertEquals(200, response.getStatus());

        // call custom user procedure: get.nodes.by.property
        json.put("query", "CALL get.nodes.by.property('Person','name','Bob')");
        response = REST_REQUEST.post("db/data/cypher", json.toString());
        assertEquals(200, response.getStatus());

        json.put("query", "CALL get.nodes.by.property('Person','name',{name})");
        json.put("params", new JSONObject().put("name", "Bob"));
        System.out.println(json.toString());
        response = REST_REQUEST.post("db/data/cypher", json.toString());
        assertEquals(200, response.getStatus());

    }
}
