package com.rina.client;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.json.JSONObject;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;


/**
 * Example RESTeasy Client code for GET and POST requests
 * Created by rinacao on 6/28/16.
 */
public class RestEasyClient {

    private static String base64encoded = Base64.getEncoder().encodeToString("neo4j:Test123".getBytes());

    public static void main (String [] args) {
        accessNeo4jUsing_resteasy_get();
        accessNeo4jUsing_resteasy_post();
    }

    /**
     * Neo4j access using RESTeasy Client GET
     */
    public static void accessNeo4jUsing_resteasy_get() {
        javax.ws.rs.client.Client client = ResteasyClientBuilder.newClient();

        WebTarget target = client.target("http://localhost:7474").path("/db/data/propertykeys");

        Response resp = target.request()
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64encoded)
                .get(Response.class);

        System.out.println("Response code: " + resp.getStatus());
        client.close();

        // call unmanaged extension
        javax.ws.rs.client.Client client1 = ResteasyClientBuilder.newClient();
        WebTarget target1 = client1.target("http://localhost:7474").path("/unmanagedExt/unmanagedExt/get");

        resp = target1.request()
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64encoded)
                .get(Response.class);

        System.out.println("Response code: " + resp.getStatus());
        client1.close();
    }

    /**
     * Neo4j access using RESTeasy Client POST to the Legacy Cypher HTTP Endpoint
     */
    public static void accessNeo4jUsing_resteasy_post() {
        javax.ws.rs.client.Client client = ResteasyClientBuilder.newClient();

        WebTarget target = client.target("http://localhost:7474").path("/db/data/cypher");

        Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64encoded);

        JSONObject cypherQuery = new JSONObject();
            cypherQuery.put("query", "CREATE (a:Person {name:'Charlie', age: 5}) RETURN a");

        Response response = invocationBuilder.post(Entity.entity(cypherQuery.toString(), MediaType.APPLICATION_JSON));

        System.out.println("Response code: " + response.getStatus());

        client.close();
    }
}
