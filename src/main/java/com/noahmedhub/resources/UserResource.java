package com.noahmedhub.resources;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.noahmedhub.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import java.util.Date;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final String MONGO_URI = "mongodb://localhost:27017";
    // 复用 MongoClient 实例（内部自带连接池，避免每次请求重连）
    private static final MongoClient MONGO_CLIENT = MongoClients.create(MONGO_URI);

    // 统一处理跨域 CORS 响应头的辅助方法
    private Response.ResponseBuilder buildCorsResponse(Response.Status status) {
        return Response.status(status)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

    // 1. 处理浏览器的 CORS OPTIONS 预检请求（解决跨域报错的关键）
    @OPTIONS
    public Response handleOptions() {
        return buildCorsResponse(Response.Status.OK).build();
    }

    @GET
    public Response checkStatus() {
        return buildCorsResponse(Response.Status.OK)
                .entity("{\"status\":\"active\"}")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        if (user == null) {
            return buildCorsResponse(Response.Status.BAD_REQUEST)
                    .entity("{\"status\":\"error\", \"message\":\"Payload cannot be null\"}")
                    .build();
        }

        try {
            MongoDatabase database = MONGO_CLIENT.getDatabase("MEDHUB");
            MongoCollection<Document> collection = database.getCollection("users");

            Document doc = new Document("firstname", user.getFirstName() != null ? user.getFirstName() : user.getFirstname())
                    .append("lastname", user.getLastName() != null ? user.getLastName() : user.getLastname())
                    .append("email", user.getEmail())
                    .append("phone", user.getPhone())
                    .append("comment", user.getComment())
                    .append("consultant1", user.getConsultant1())
                    .append("consultant2", user.getConsultant2())
                    .append("consultant3", user.getConsultant3())
                    .append("createdAt", new Date().toString());

            collection.insertOne(doc);

            return buildCorsResponse(Response.Status.CREATED)
                    .entity("{\"status\":\"success\", \"message\":\"User saved successfully to MEDHUB!\"}")
                    .build();

        } catch (MongoWriteException e) {
            // 捕获 MongoDB 唯一索引冲突 (Error Code 11000)
            if (e.getError().getCode() == 11000) {
                return buildCorsResponse(Response.Status.CONFLICT) // 409 Conflict
                        .entity("{\"status\":\"error\", \"message\":\"The email has already been used!\"}")
                        .build();
            }
            return buildCorsResponse(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"error\", \"message\":\"Database write error: " + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return buildCorsResponse(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}