package com.noahmedhub.resources;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Path("/faq")
@Produces(MediaType.APPLICATION_JSON)
public class FaqResource {

   // private static final String MONGO_URI = "mongodb://localhost:27017";
	private static final String MONGO_URI = System.getenv("MONGODB_URI") != null ? 
            System.getenv("MONGODB_URI") : "mongodb://localhost:27017";
	private static final MongoClient MONGO_CLIENT = MongoClients.create(MONGO_URI);

    private Response.ResponseBuilder buildCorsResponse(Response.Status status) {
        return Response.status(status)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

    @OPTIONS
    public Response handleOptions() {
        return buildCorsResponse(Response.Status.OK).build();
    }

    @GET
    public Response getAllFaqs() {
        try {
          //  MongoDatabase database = MONGO_CLIENT.getDatabase("MEDHUB");
        	MongoDatabase database = MONGO_CLIENT.getDatabase("NoahMedhubDB");
        	MongoCollection<Document> collection = database.getCollection("FAQ");

            List<Document> faqs = new ArrayList<>();
            for (Document doc : collection.find()) {
                // 将 ObjectId 转换成字符串形式的 id，便于前端绑定 key
                if (doc.containsKey("_id")) {
                    doc.put("id", doc.getObjectId("_id").toString());
                    doc.remove("_id");
                }
                faqs.add(doc);
            }

            return buildCorsResponse(Response.Status.OK)
                    .entity(faqs)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return buildCorsResponse(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}