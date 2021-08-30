package com.sixt.httpcomparator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.asynchttpclient.*;

import java.awt.event.InputEvent;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import static io.restassured.RestAssured.given;


public class APIResponseComparator {


    HttpClient client = HttpClient.newHttpClient();

    public void InputFilePathAsyc(String file1, String file2)  {
        BufferedReader buff1 = null;
        BufferedReader buff2 = null;

        try {

            String api1 = null;
            String api2 = null;

            buff1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8), 1000 * 8192);
            buff2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8), 1000 * 8192);
            while ((api1 = buff1.readLine()) != null && (api2 = buff2.readLine()) != null)
            {
                apiComparator(api1, api2);
            }

        }
        catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void apiComparator(String api1, String api2) throws InterruptedException, ExecutionException, IOException {

        CompletableFuture<InputStream> file1API = gethttpasync(api1);
        CompletableFuture<InputStream> file2API = gethttpasync(api2);

        InputStream resp1 = file1API.get();
        InputStream resp2 = file2API.get();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode1 = objectMapper.readTree(resp1);
        JsonNode jsonNode2 = objectMapper.readTree(resp2);

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

    public CompletableFuture<InputStream> gethttpasync(String uri) {

        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(uri))
                .build();

        return client.sendAsync(request, BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body);

    }


    public org.asynchttpclient.Response AsynchronousFunction(String URL) throws ExecutionException, InterruptedException, JsonProcessingException
    {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        //org.asynchttpclient.Response response = client.prepareGet(URL).execute().get();
        Future<org.asynchttpclient.Response> f = client.prepareGet(URL).execute();
        org.asynchttpclient.Response resp = f.get();
        return (org.asynchttpclient.Response) resp;

    }

    public void InputFilePath5(String file1, String file2) throws IOException {
        BufferedReader buff1 = null;
        BufferedReader buff2 = null;
        String line1 = null;
        String line2 = null;

        try {
            buff1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
            buff2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8));
            while ((line1 = buff1.readLine()) != null && (line2 = buff2.readLine()) != null) {
                CloseableHttpResponse resp1 = httprequest(line1);
                CloseableHttpResponse resp2 = httprequest(line2);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode1 = objectMapper.readTree(resp1.getEntity().getContent());
                JsonNode jsonNode2 = objectMapper.readTree(resp2.getEntity().getContent());
                if (jsonNode1.equals(jsonNode2)) {
                    System.out.println(line1 + "  equals  " + line2);
                } else {
                    System.out.println(line1 + "  not equals  " + line2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            assert buff1 != null;
            buff1.close();
            assert buff2 != null;
            buff2.close();
        }
    }


    public CloseableHttpResponse httprequest(String URL) throws IOException
    {
        HttpClientConnectionManager poolingConnManager
                = new PoolingHttpClientConnectionManager();
        CloseableHttpClient client
                = HttpClients.custom().setConnectionManager(poolingConnManager)
                .build();
        CloseableHttpResponse resp = client.execute(new HttpGet(URL));
        return resp;

    }
}
