package com.example.rest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Path("/customerform")
public class CustomerRestForm {
    /**
     * Class for holding the list of customers and handling the requests
     */

    private static ArrayList<Customer> customers = new ArrayList<>();

    /**
     * Meant for returning the list of customers
     * @return A concatenation of the toString method for all customers
     */
    @GET
    @Produces("application/xml")
    public ArrayList<Customer> getCustomer() {
        return customers;
    }

    /**
     * Meant for getting a customer with a specific ID
     * @param id of the customer
     * @return toString method of customer
     */
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Customer getCustomerList(@PathParam("id") int id) {
        Customer customer = customers.stream().filter(customer1 -> customer1.getId() == id)
                .findFirst()
                .orElse(null);
        return customer;
    }

    /**
     * Meant for creating customers using the post method
     * @param name of the customer
     * @param age of the customer
     */
    @POST
    public Response createCustomer(@HeaderParam("x-api-key") String token,
                                   @FormParam("name") String name, @FormParam("age") int age) throws GeneralSecurityException, IOException {
        if(validateToken(token)){
            Customer newCustomer = new Customer(name, age);
            customers.add(newCustomer);
            return Response.status(Response.Status.OK).entity("Succesfully added customer!").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated!").build();
    }

    /**
     * Meant for replacing customer with specific ID
     * @param id of the customer
     * @param name of the customer
     * @param age of the customer
     */
    @PUT
    @Path("{id}")
    public Response modifyCustomer(@HeaderParam("x-api-key") String token,
                                   @PathParam("id") int id, @FormParam("name") String name, @FormParam("age") int age) {
        if(validateToken(token)){
            Customer customer = customers.stream().filter(customer1 -> customer1.getId() == id)
                    .findFirst()
                    .orElse(null);
            if(customer != null){
                customer.setAge(age);
                customer.setName(name);
                return Response.status(Response.Status.OK).entity("Succesfully modified customer!").build();
            }
            else{
                return Response.status(Response.Status.OK).entity("Customer does not exist!").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated!").build();
    }

    /**
     * Meant for deleting customer with specific ID
     * @param id of the customer
     */
    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@HeaderParam("x-api-key") String token, @PathParam("id") int id) {
        if(validateToken(token)){
            customers = customers.stream().filter(customer -> customer.getId() != id)
                    .collect(Collectors.toCollection(ArrayList::new));
            return Response.status(Response.Status.OK).entity("Succesfully deleted customer!").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated!").build();
    }

    /**
     * This method sends a POST request to the User API, to verify that a call is done
     * by an authenticated user
     * @param token generated from login
     * @return boolean if the user is authenticated or not
     */
    private boolean validateToken(String token) {
        // Setting the truststore to our server.jks file let Java trust the certificate.
        System.setProperty("javax.net.debug", "ssl");
        System.setProperty("javax.net.ssl.trustStore","localhost.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(String.format("https://localhost:8080/restaurant/user/auth"));
            httpPost.addHeader("x-api-key", token);
            CloseableHttpResponse httpResponse = client.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String isAuthenticated = EntityUtils.toString(entity);
            httpResponse.close();
            if(isAuthenticated.equals("true")){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
