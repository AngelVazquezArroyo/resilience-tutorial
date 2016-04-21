# Getting started
The goal of this tutorial is to implement several resilience patterns. For this purpose use a
subset of the design exercise, these Recommendation Service and an underlying analysis service. To focus on coding, we decide to implement the Recommendation Service as a [Dropwizard web application](http://www.dropwizard.io/0.9.2/docs/manual/index.html). We choose a REST-based communication style as we assumed that most of you are using that on a more regular basis than message-based or event-based communication.

In dependence of the recommendation by the DropWizard Project our service look like this:

* **de.codecentric.recommendationService**
  * **api.Recommendation** : A JSON Representation of the entity in the Service API - the proper Recommendation.
  * **client.AnalysisServiceClient** : Integration of the Analysis Service via Apaches HttpClient
   Library.
  * **core**: Some more domain implementation.
  * **health**: Some Health Checks to test the "wellness" of the Recommendation Service.
    * **AnalysisServiceHealthCheck**: check if the AnalysisService is available.
    * **RequestHealthCheck**: check if the number of requests is in the "normal" range.
  * **resources.RecommendationResource**: the exposed resource (RESTful API) by the Recommendation Service. This Class is the main place where you should implement.
  * **MyApplication**: The main entry point into the Recommendation Service.
* **MyApplicationConfiguration**: provides a number of built-in configuration parameters for the Recommendation Service.

# start the engine(s)

Go to the repo/project directory and change to service folder

```
cd recommendationService
```

perform the maven build process

```
mvn clean test
```

The build process contains some basic test, to ensure the basic functionality of the Recommendation Service.

Now that you’ve built a JAR file, it’s time to run it. Go to the project directory and execute
(or use your IDE and don't forget the set the parameters in the configuration):

```
java -jar target/recommendation-service-1.0-SNAPSHOT.jar server src/main/resources/recommendationServiceConfiguration.yml
```

The Service is now listening on port 8102 for service requests and on port 8202 for administration requests. Go To browser an execute some service requests:

```
http://localhost:8101/recommendation?user=U001&product=P00T
{"code":500,"message":"There was an error processing your request. It has been logged (ID 378a8897b0cff720)."}
```

And the administration request shows the cause:

```
http://localhost:8201/healthcheck
{"AnalysisService":{"healthy":false,"message":"Analysis service not accessible"},"deadlocks":{"healthy":true}}
``

The Recommendation Service send a request to another service - the Analysis Service. At the
moment we don't have an implementation nor a runtime unit. So we have to cheat our Recommendation Service and use an impostor ([here you can find more information about it](https://github.com/ufried/impostor)). Open a terminal/console, got to the project folder and execute the following commands:

````
cd impostor/"{darwin_amd64 | linux_amd64 |	windows_amd64}"
impostor localhost:8102
```

and configure the impostor:

```
curl -i -H "Content-Type: application/json" -d "@./target/test-classes/impostor/config/downstream_normal.json" http://localhost:8102/config
```

Now all health checks should report success, a 200 OK is the returned. The Recommendation Service Request receive a pleasant response, too.  

```
http://localhost:8201/healthcheck
{"AnalysisService":{"healthy":true,"message":"Analysis service accessible"},"deadlocks":{"healthy":true}}

http://localhost:8101/recommendation?user=U001&product=P000
{"user":"U001","products":["P001"]}
```

Now shut down all service gracefully by pressing ^C.

> NOTE: _You don't need to start and stop the Recommendation Service manually from now. The test
accepts that for you. Start, configure and stop the impostor does the particular test, too._

# Some general agreements for the next steps

* You can do these exercises alone or team up with another person in the room – whatever you prefer.
* Copy the new test file from the exercise folder to the appropriate test folder in the Recommendation Service project.
* The task basically is to get the tests green. If you run them now, they should all fail.
* Then, you should add the appropriate checks to make the tests become green
* You will have about 20-40m time for the exercise. Afterwards we will briefly discuss your findings and ideas to further improve it

There is also a sample solution in the exercise folder. Please, do not spoil yourself the fun and
 look into it before you tried it yourself

After you have done it, you can have a look at it of course ... and maybe you will come to the conclusion that you prefer your solution ... ;)

Here we go!
