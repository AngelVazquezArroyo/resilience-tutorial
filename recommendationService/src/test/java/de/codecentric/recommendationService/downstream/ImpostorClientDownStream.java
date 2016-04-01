package de.codecentric.recommendationService.downstream;

import de.codecentric.recommendationService.clients.ImpostorClient;
import de.codecentric.recommendationService.clients.ImpostorCommands;
import de.codecentric.recommendationService.clients.ImpostorConfig;
import de.codecentric.recommendationService.clients.ServiceClientException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorClientDownStream implements ImpostorClient {

    private static final Logger logger = LoggerFactory.getLogger(ImpostorClientDownStream.class);

    private String host;
    private int port;
    private HttpClient client;

    public ImpostorClientDownStream(String host, int port, HttpClient client) {
        this.host = host;
        this.port = port;
        this.client = client;
    }

    @Override
    public void setConfig(ImpostorConfig config) throws ServiceClientException{

        HttpPost downStreamConfigRequest = null;
        URI downStreamConfigURI;

//            System.out.println("setconfig to " + config.getJSon());

        try {
            downStreamConfigURI = new URIBuilder()
                    .setScheme("http")
                    .setHost(this.host)
                    .setPort(this.port)
                    .setPath("/config")
                    .build();

        downStreamConfigRequest = new HttpPost(downStreamConfigURI);

            logger.info("DownStreamConfig: " + config.toString());

            StringEntity requestJson = new StringEntity(config.getJSon(), ContentType.APPLICATION_JSON);
            downStreamConfigRequest.setEntity(requestJson);

            HttpResponse response = null;
            response = client.execute(downStreamConfigRequest);

            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                logger.info("set config to: " + config.toString() + " was successfull");
            } else {
                throw new ServiceClientException("set config to :" + config.toString() + " was not successfull. Status: " + status + " " + response.getEntity().toString());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeCommand(ImpostorCommands command) {
        // no commands at the moment
    }
}


