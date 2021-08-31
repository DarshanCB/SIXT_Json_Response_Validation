package com.sixt.httpcomparator;

import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class APIResponseComparator {

    HttpClient client = HttpClient.newHttpClient();

    public void inputResponseComparator(String file1, String file2) throws IOException {
        BufferedReader buff1 = null;
        BufferedReader buff2 = null;

        try {

            String api1 = null;
            String api2 = null;

            buff1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8), 1000 * 8816);
            buff2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8), 1000 * 8816);

            while ((api1 = buff1.readLine()) != null && (api2 = buff2.readLine()) != null)
            {
                apiComparator(api1, api2);
            }

        }
        catch (IOException | InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            assert buff1 != null;
            buff1.close();
            assert buff2 != null;
            buff2.close();
        }

    }

    private void apiComparator(String api1, String api2) throws InterruptedException, ExecutionException, IOException
    {
        CompletableFuture<InputStream> file1API = getHttpAsync(api1);
        CompletableFuture<InputStream> file2API = getHttpAsync(api2);

        InputStream resp1 = file1API.get();
        InputStream resp2 = file2API.get();

    try
    {
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
    catch (IOException e)
    {
        e.printStackTrace();
        resp1.close();
        resp2.close();
    }


    }

    public CompletableFuture<InputStream> getHttpAsync(String uri) {

        HttpRequest request = null;

        try {
            request = HttpRequest.newBuilder().
                    uri(URI.create(uri))
                    .build();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body);

    }

}
