package com.rina.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Example Jersey Client code for GET and POST requests
 * Created by rinacao on 6/28/16.
 */
public class JerseyClient {

    public static void main(String[] args) {
        accessNeo4jUsing_jersey_get();
        accessNeo4jUsing_jersey_post_json();
        accessNeo4jUsing_jersey_post_pojoMapping();
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
    public static void accessNeo4jUsing_jersey_post_json() {
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

    public static void accessNeo4jUsing_jersey_post_pojoMapping (){
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("neo4j", "Test123"));

        WebResource webResource = client.resource("http://localhost:7474/unmanagedExt/unmanagedExt/postJson");

        Map<String,Object> postBody = new HashMap<>();
        postBody.put("name","PowerReviews");
        postBody.put("location","chicago");
        ClientResponse response = webResource.accept("application/json")
                .type("application/json").post(ClientResponse.class, postBody);

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

        response.close();
        client.destroy();
    }
}
