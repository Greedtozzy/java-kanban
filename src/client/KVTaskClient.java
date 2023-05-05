package client;

import com.google.gson.Gson;
import exceptions.ClientException;
import service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVTaskClient {
    private static final String URI_FORMAT = "http://localhost:8079";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = Managers.getGson();
    public final String token;

    public KVTaskClient() {
        this.token = getApiToken();
    }

    private String getApiToken() {
        URI uri = URI.create(URI_FORMAT + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), String.class);
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Не получилось загрузить токен", e);
        }
    }

    public void put(String key, String json) {
        URI uri = URI.create(URI_FORMAT + "/save/" + key + "?API_TOKEN=" + token);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json, UTF_8))
                .uri(uri)
                .header("X-API-KEY", token)
                .build();

        try {
//            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Что-то пошло не так во время сохранения.", e);
        }
    }

    public String load(String key) {
        URI uri = URI.create(URI_FORMAT + "/load/" + key + "?API_TOKEN=" + token);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("X-API-KEY", token)
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Что-то пошло не так во время загрузки", e);
        }
    }
}
