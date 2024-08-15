package com.example.Networking.Server;

public class Response {

    public enum Status {
        // Rest api Response protocols
        OK("200 OK"), // 200 OK: The request was successful.
        NOT_FOUND("404 Not Found"), // 404 Not Found: The server cannot find the requested resource.
        BAD_REQUEST("400 Bad Request"), // 400 Bad Request: The server could not understand the request due to invalid syntax
        UNAUTHORIZED("401 Unauthorize"); // 401 Unauthorized: The client must authenticate itself to get the requested response.
        
        private final String status;

        Status(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    private Status status;
    private Object content;

    public Response(Status status, Object content) {
        this.status = status;
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public Object getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Response{status=" + status + ", content=" + content + "}";
    }

    // --------------->     TEST FUNCTIONS  <----------------------------
    public static void testResponseString() {
        Response successResponse = new Response(Response.Status.OK, "Data retrieved successfully");
        Response notFoundResponse = new Response(Response.Status.NOT_FOUND, "Resource not found");

        System.out.println(successResponse);  
        System.out.println(notFoundResponse); 

        System.out.println("Status: " + successResponse.getStatus());  
        System.out.println("Content: " + successResponse.getContent()); 
    }

    // MAIN FUNCTION
    public static void main(String[] args) {
    }
}
