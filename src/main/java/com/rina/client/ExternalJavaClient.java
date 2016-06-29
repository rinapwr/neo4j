package com.rina.client;

import org.neo4j.driver.v1.*;


/**
 * Created by rinacao on 6/28/16.
 */
public class ExternalJavaClient {

    public static void main (String [] args) {
        queryNeo4jFromExternalJavaClient_bolt();
    }

    /**
     * Neo4j query access using bolt protocol
     * - Cypher from external JVM using Cypher client and prepared statements
     */
    public static void queryNeo4jFromExternalJavaClient_bolt () {

        Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "Test123"));
        Session session = driver.session();

        session.run( "CREATE (a:Person {name:'Arthur', title:'King'})" );
        StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = 'Arthur' " +
                "RETURN a.name AS name, a.title AS title" );

        while ( result.hasNext() )
        {
            Record record = result.next();
            System.out.println( record.get( "title" ).asString() + " " + record.get("name").asString() );
        }
        session.close();
        driver.close();
    }
}
