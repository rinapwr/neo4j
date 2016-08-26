package com.rina.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Example Jersey Client code for GET and POST requests
 * Created by rinacao on 6/28/16.
 */
public class JerseyClient {

    public static void main(String[] args) {
//        accessNeo4jUsing_jersey_get();
//        legacy_accessNeo4jUsing_jersey_post_json();
//        legacy_accessNeo4jUsing_jersey_post_pojoMapping();
        accessNeo4jUsingTransactionalHttpEndpoint_jersey_post_multiple_stmts_index();
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
    public static void legacy_accessNeo4jUsing_jersey_post_json() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("neo4j", "Test123"));
        WebResource webResource = client.resource("http://localhost:7474/db/data/cypher");

        String input = "{\"query\":\"CREATE (a:Person {name:'Jersey bob', age: 5}) RETURN a\"}";

        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

        response.close();
        client.destroy();
    }

    /**
     * Example for posting json using pojo mapping
     */
    public static void legacy_accessNeo4jUsing_jersey_post_pojoMapping(){
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("neo4j", "Test123"));

        WebResource webResource = client.resource("http://localhost:7474/db/data/cypher");

        Map<String,Object> postBody = new HashMap<>();
        postBody.put("query", "CREATE (a:Person {name:'Jersey Charlie', age: 5}) RETURN a");
        ClientResponse response = webResource.accept("application/json")
                .type("application/json").post(ClientResponse.class, postBody);

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

        response.close();
        client.destroy();
    }

    /**
     * Neo4j access using Jersey Client POST to the Transactional Cypher HTTP Endpoint
     * http://neo4j.com/docs/snapshot/rest-api-transactional.html
     */
    public static void accessNeo4jUsingTransactionalHttpEndpoint_jersey_post_single_stmt() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("neo4j", "Test123"));

        WebResource webResource = client.resource("http://localhost:7474").path("db/data/transaction/commit");

        Map<String,Object> postBody = new HashMap<>();

        Map<String,Object> stmt1 = new HashMap<>();
        stmt1.put("statement","CREATE (n:Person{name:'rina1'}) RETURN id(n)");
        postBody.put("statements", Arrays.asList(stmt1));

        ClientResponse response = webResource.accept("application/json")
                .type("application/json").post(ClientResponse.class, postBody);

        System.out.println("Output from Server .... \n");
        Object output = response.getEntity(String.class);
        System.out.println(output);

        response.close();
        client.destroy();
    }

    /**
     * Neo4j access using Jersey Client POST to the Transactional Cypher HTTP Endpoint
     * http://neo4j.com/docs/snapshot/rest-api-transactional.html
     */
    public static void accessNeo4jUsingTransactionalHttpEndpoint_jersey_post_multiple_stmts_index() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter("neo4j", "Test123"));

//        WebResource webResource = client.resource("http://ip-10-101-219-23.dev.us-west-2.pwr:7474")
//                .path("db/data/transaction/commit");
        WebResource webResource = client.resource("http://localhost:7474")
                .path("db/data/transaction/commit");

        Map<String,Object> postBody = new HashMap<>();

        Map<String,Object> stmt1 = new HashMap<>();
        stmt1.put("statement","CREATE INDEX ON :Product(pfd_id)");
        Map<String,Object> stmt2 = new HashMap<>();
        stmt2.put("statement","CREATE INDEX ON :Catalog(catalog_id)");
        Map<String,Object> stmt3 = new HashMap<>();
        stmt3.put("statement","CREATE INDEX ON :Brand(brand)");

        postBody.put("statements", Arrays.asList(stmt1, stmt2, stmt3));

        ClientResponse response = webResource.accept("application/json")
                .type("application/json").post(ClientResponse.class, postBody);

        System.out.println("Output from Server .... \n");
        Object output = response.getEntity(String.class);
        System.out.println(output);

        response.close();
        client.destroy();
    }
}