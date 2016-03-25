package de.codecentric.testrunner.clients.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.testrunner.clients.ServiceClient;
import de.codecentric.testrunner.clients.ServiceClientException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by afitz on 24.03.16.
 */
public class ServiceClientAwesomeRecommendation implements ServiceClient {

    private String host;
    private int port;
    private int portAdmin;
    private String path;
    private HttpClient httpClient;

    private URI serviceURI;
    private URI healthyURI;

    private HttpGet getHealthy;

    private ObjectMapper mapper = new ObjectMapper();

    public ServiceClientAwesomeRecommendation(String host, int port, int portAdmin, String path, HttpClient httpClient) {
        this.host = host;
        this.port = port;
        this.portAdmin = portAdmin;
        this.path = path;
        this.httpClient = httpClient;

        System.out.println(this.host + ":" + this.port + ":" + this.portAdmin + this.path);

        try {
            serviceURI = new URIBuilder()
                    .setScheme("http")
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath(this.path)
                    .build();

            System.out.println("serviceURI success");

            healthyURI = new URIBuilder()
                    .setScheme("http")
                    .setHost(this.host)
                    .setPort(this.portAdmin)
                    .setPath("/healthcheck")
                    .build();

            System.out.println("HealthyURI success");

            getHealthy = new HttpGet(healthyURI);

            System.out.println("getHealthy success");

        } catch (URISyntaxException e) {
            System.out.println("Build URI");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error Constructor: " + e.getMessage());
        }
    }

    @Override
    public ServiceHealthResult getHealthy(ServiceHealthStatusCode check) throws ServiceClientException {
        // tbd!: extract the requestet health status
        return this.getHealthy();
    }

    @Override
    public ServiceHealthResult getHealthy() throws ServiceClientException {

//        System.out.println("getHealthy");

        Map<String, ServiceHealthStatusStructure> healthyMap = null;

        try {

            // Create a custom response handler
            ResponseHandler<ServiceHealthResult> responseHandler = new ResponseHandler<ServiceHealthResult>() {

                public ServiceHealthResult handleResponse(final HttpResponse response) throws IOException {

                    int status = response.getStatusLine().getStatusCode();
                    String message;
                    String entityString;

                    entityString = (response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "no health checks");

                    if (status >= 200 && status < 300) {
                        return new ServiceHealthResult(status, "service seems to be healthy", entityString);
                    } else {
                        return new ServiceHealthResult(status, "Unexpected response status: " + status, entityString);
                    }
                }

            };


            return httpClient.execute(getHealthy, responseHandler);

        } catch (ClientProtocolException e) {
            throw new ServiceClientException("ServiceClient ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            throw new ServiceClientException("ServiceClient IOException: " + e.getMessage());
        } catch (Exception e) {
            throw new ServiceClientException("ServiceClient UnknownException: " + e.getMessage());
        }
    }
}
