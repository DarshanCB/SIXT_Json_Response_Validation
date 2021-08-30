package com.sixt.httpcomparator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.asynchttpclient.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;


public class APIResponseComparator {


    HttpClient client = HttpClient.newHttpClient();

    public void InputFilePathAsyc(String file1, String file2) throws IOException {
        BufferedReader buff1 = null;
        BufferedReader buff2 = null;

        try {

            String api1 = null;
            String api2 = null;

            buff1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
            buff2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8));

            while ((api1 = buff1.readLine()) != null && (api2 = buff2.readLine()) != null)
            {
                apiComparator(api1, api2);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void apiComparator(String api1, String api2) throws InterruptedException, ExecutionException, IOException {

        CompletableFuture<String> file1API = gethttpasync(api1);
        CompletableFuture<String> file2API = gethttpasync(api2);

        String resp1 = file1API.get();
        String resp2 = file2API.get();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode1 = objectMapper.readTree(resp1.getBytes(StandardCharsets.UTF_8));
        JsonNode jsonNode2 = objectMapper.readTree(resp2.getBytes(StandardCharsets.UTF_8));

        if (jsonNode1.equals(jsonNode2))
        {
            System.out.println(api1 + "  equals  " + api2);
        } else
        {
            System.out.println(api1 + "  not equals  " + api2);
        }

    }



    public void InputFilePath(String file1, String file2) throws IOException {
        BufferedReader buff1 = null;
        BufferedReader buff2 = null;
        String line1 = null;
        String line2 = null;

        try {
            buff1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
            buff2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8));
            while ((line1 = buff1.readLine()) != null && (line2 = buff2.readLine()) != null) {
                String resp1 = gethttp(line1);
                String resp2 = gethttp(line2);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode1 = objectMapper.readTree(resp1.getBytes(StandardCharsets.UTF_8));
                JsonNode jsonNode2 = objectMapper.readTree(resp2.getBytes(StandardCharsets.UTF_8));
                if (jsonNode1.equals(jsonNode2)) {
                    System.out.println(line1 + "  equals  " + line2);
                } else {
                    System.out.println(line1 + "  not equals  " + line2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public void InputFilePath3(String file1, String file2) throws IOException {
        BufferedReader buff1 = null;
        BufferedReader buff2 = null;
        String line1 = null;
        String line2 = null;

        try {
            buff1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
            buff2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8));
            while ((line1 = buff1.readLine()) != null && (line2 = buff2.readLine()) != null) {
                    compare_response(line1, line2);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            assert buff1 != null;
            buff1.close();
            assert buff2 != null;
            buff2.close();
        }
        

    }

    public void InputFilePath4(String file1, String file2) throws IOException {
        BufferedReader buff1 = null;
        BufferedReader buff2 = null;
        String line1 = null;
        String line2 = null;

        try {
            buff1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
            buff2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8));
            while ((line1 = buff1.readLine()) != null && (line2 = buff2.readLine()) != null) {
                org.asynchttpclient.Response resp1 = AsynchronousFunction(line1);
                org.asynchttpclient.Response resp2 = AsynchronousFunction(line2);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode1 = objectMapper.readTree(resp1.getResponseBody());
                JsonNode jsonNode2 = objectMapper.readTree(resp2.getResponseBody());
                if (jsonNode1.equals(jsonNode2)) {
                    System.out.println(line1 + "  equals  " + line2);
                } else {
                    System.out.println(line1 + "  not equals  " + line2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            assert buff1 != null;
            buff1.close();
            assert buff2 != null;
            buff2.close();
        }


    }



    public void compare_response(String URL1, String URL2) throws JsonProcessingException
    {
        try {
            Response response1 = given().contentType(ContentType.JSON).get(URL1);
            Response response2 = given().contentType(ContentType.JSON).get(URL2);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode1 = objectMapper.readTree(response1.getBody().asString());
            JsonNode jsonNode2 = objectMapper.readTree(response2.getBody().asString());

            if (jsonNode1.equals(jsonNode2)) {
                System.out.println(URL1 + "  equals  " + URL2);
            } else {
                System.out.println(URL1 + "  not equals  " + URL2);
            }
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    public String gethttp(String uri) throws Exception {

        HttpResponse<String> response = null;

        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri)).build();

            response = client.send(request, BodyHandlers.ofString());

        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        assert response != null;
        return response.body();

    }

    public CompletableFuture<String> gethttpasync(String uri) {

        HttpRequest request = null;

        try
        {
            request = HttpRequest.newBuilder().uri(URI.create(uri))
                    .build();

        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        return client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

    }


    public org.asynchttpclient.Response AsynchronousFunction(String URL) throws ExecutionException, InterruptedException, JsonProcessingException
    {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable service1 = new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient client = Dsl.asyncHttpClient();
                //org.asynchttpclient.Response response = client.prepareGet(URL).execute().get();
                Future<org.asynchttpclient.Response> f = client.prepareGet(URL).execute();
                org.asynchttpclient.Response resp = null;
                try {
                    resp = f.get();
                    return (org.asynchttpclient.Response) resp;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }


        AsyncHttpClient client = Dsl.asyncHttpClient();
        //org.asynchttpclient.Response response = client.prepareGet(URL).execute().get();
        Future<org.asynchttpclient.Response> f = client.prepareGet(URL).execute();
        org.asynchttpclient.Response resp = f.get();
        return (org.asynchttpclient.Response) resp;


    }


}
