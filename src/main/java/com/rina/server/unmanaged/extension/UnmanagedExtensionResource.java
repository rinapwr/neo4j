package com.rina.server.unmanaged.extension;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.logging.Log;
import org.neo4j.string.UTF8;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path( "/unmanagedExt" )
public class UnmanagedExtensionResource
{
    private final GraphDatabaseService database;

    public UnmanagedExtensionResource(@Context GraphDatabaseService database )
    {
        this.database = database;
    }

    @GET
    @Path( "/getGraphData" )
    public String getGraphData( )
    {
        // return a random node
        Result r = database.execute("MATCH (n) RETURN n LIMIT 1");
        return r.resultAsString();
    }

    @GET
    @Path( "/get" )
    public String get( )
    {
        return  "Hello World, this is a GET test." ;
    }

    @POST
    @Path( "/post" )
    public Response post( String name )
    {
        // Do stuff with the database
        return Response.status( Status.OK ).entity( UTF8.encode( "Hello World, this is a POST test from user " + name ) ).build();
    }

    @POST
    @Path("/postJson")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postJSON(String data) {
        String result = "Data post: " + data;
        return Response.status( Status.OK ).entity(result).build();

    }

    @GET
    @Produces( MediaType.TEXT_PLAIN )
    @Path( "/{nodeId}" )
    public Response hello( @PathParam( "nodeId" ) long nodeId )
    {
        // Do stuff with the database
        return Response.status( Status.OK ).entity( UTF8.encode( "Hello World, nodeId=" + nodeId ) ).build();
    }
}