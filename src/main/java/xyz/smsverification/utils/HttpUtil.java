package xyz.smsverification.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import xyz.smsverification.exception.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class HttpUtil {
    private final String apiKey;

    @Getter
    private final HttpClient httpClient;

    @Getter @Setter
    private boolean debug = false;

    public HttpUtil(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClients.createMinimal();
    }

    public JSONObject request(String endpoint, String body, String method) throws Exception {
        val res = sendRequest(endpoint, body, method);

        val status = res.getString("status");
        if (status.equals("success")) {

            return res.getJSONObject("data");

        } else if (status.equals("error")) {
            val errorType = res.getString("error_type");
            val errorContent = res.getString("content");

            if (errorType.equals("WRONG_AUTH")) {
                throw new InvalidCredentialsException(res.toString());
            }

            if (errorType.equals("WAITING_FOR_INCOMING_MESSAGES")) {
                throw new WaitingForMessagesException(res.toString());
            }

            if (errorType.equals("UNKNOWN")) {

                if (errorContent.startsWith("Failed to get resource.")) {
                    throw new OrderUnavailableException(res.toString());
                }

            }

        }

        throw new UnexpectedApiErrorException(res.toString());
    }

    private JSONObject sendRequest(String endpoint, String body, String method) throws Exception {
        // Create new request allowing entities no matter the method
        val request = new HttpRequestWithEntity(method, new URI(endpoint));

        // Auth
        request.setHeader("Authentication", this.apiKey);

        if (body != null) {
            val stringEntity = new StringEntity(body);
            request.setEntity(stringEntity);
        }

        val response = this.httpClient.execute(request);

        val responseString = streamToString(response.getEntity().getContent()).replace("\n", "");

        if (this.debug) {
            System.out.println("[DEBUG (res code: " + response.getStatusLine().getStatusCode() + ")] " + responseString);
        }

        if (responseString.length() == 0) {
            throw new InvalidApiResponseException("API returned empty body");
        }

        return new JSONObject(responseString);
    }


    private String streamToString(InputStream inputStream) throws Exception {
        val out = new StringBuilder();

        BufferedReader isReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String string;
            while ((string = isReader.readLine()) != null) {
                out.append(string);
                out.append("\n");
            }
        } catch (Exception e) {
            isReader.close();
            throw e;
        }
        isReader.close();

        return out.toString();
    }

    public static class HttpRequestWithEntity extends HttpPost {
        @Setter
        public String methodName;

        public HttpRequestWithEntity(String methodName, URI uri) {
            super();

            this.methodName = methodName;
            this.setURI(uri);
        }

        @Override
        public String getMethod() {
            return methodName;
        }
    }

}
