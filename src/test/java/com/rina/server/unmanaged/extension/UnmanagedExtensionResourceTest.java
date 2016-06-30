package com.rina.server.unmanaged.extension;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.server.rest.JaxRsResponse;
import org.neo4j.server.rest.RestRequest;
import org.neo4j.shell.util.json.JSONObject;

public class UnmanagedExtensionResourceTest
{
    private RestRequest REST_REQUEST;

    @Rule
    public Neo4jRule server = new Neo4jRule().withExtension("/test", UnmanagedExtensionResource.class);

    @Before
    public void setUp() throws Exception {
        System.out.println(server.httpURI());
        REST_REQUEST = new RestRequest(server.httpURI());
    }

    @Test
    public void testUnmanagedExtension_GET ()
    {
        JaxRsResponse response = REST_REQUEST.get("test/unmanagedExt/get");
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("Hello World, this is a GET test.", response.getEntity());
        response.close();
    }

    @Test
    public void testUnmanagedExtension_GetGraphData() throws Exception
    {
        // create node in db
        JSONObject json = new JSONObject();
        JaxRsResponse response1 = null, response2 = null;
        try {
            json.put("query", "CREATE (a:Person {name:'Alice', age: 18}) RETURN a");
            response1 = REST_REQUEST.post("db/data/cypher", json.toString());
            Assert.assertEquals(200, response1.getStatus());

            // query for node
            response2 = REST_REQUEST.get("test/unmanagedExt/getGraphData");
            Assert.assertEquals(200, response2.getStatus());
            System.out.println(response2.getEntity());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (response1 != null){
                response1.close();
            }
            if (response2 != null){
                response2.close();
            }
        }
    }

    @Test
    public void testUnmanagedExtension_POST ()
    {
        String username = "ABCDE";
        JaxRsResponse response = REST_REQUEST.post("test/unmanagedExt/post", username);
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("Hello World, this is a POST test from user " + username, response.getEntity());
        response.close();
    }

    @Test
    public void testUnmanagedExtension_jsonPOST ()
    {
        JSONObject json = new JSONObject();
        JaxRsResponse response = null;
        try {
            json.put("name", "POST");
            json.put("inputType","JSON");
            json.put("x","y");

            response = REST_REQUEST.post("test/unmanagedExt/postJson", json.toString());

            Assert.assertEquals(200, response.getStatus());
            Assert.assertEquals("Data post: "+json.toString(), response.getEntity());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            response.close();
        }
    }
}
