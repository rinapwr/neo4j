package com.rina.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * Example Jersey Client code for GET and POST requests
 * Created by rinacao on 6/28/16.
 */
public class JerseyClient {

    public static void main(String[] args) {
        accessNeo4jUsing_jersey_get();
        accessNeo4jUsing_jersey_post();
    }

    /**
     * Neo4j access using Jersey Client GET
     */
    public static void accessNeo4jUsing_jersey_get() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("neo4j", "Test123"));
        WebResource webResource = client.resource("http://localhost:7474/unmanagedExt/unmanagedExt/get");

        ClientResponse response = webResource.type("application/text")
                .get(ClientResponse.class);

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

        response.close();
        client.destroy();
    }

    /**
     * Neo4j access using Jersey Client POST to the Legacy Cypher HTTP Endpoint
     */
    public static void accessNeo4jUsing_jersey_post() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("neo4j", "Test123"));
        WebResource webResource = client.resource("http://localhost:7474/unmanagedExt/unmanagedExt/postJson");

        String input = "{\"name\":\"ABC\"}";

        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

        response.close();
        client.destroy();
    }
}
