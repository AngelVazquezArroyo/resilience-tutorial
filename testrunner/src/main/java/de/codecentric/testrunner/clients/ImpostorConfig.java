package de.codecentric.testrunner.clients;


/**
 * Created by afitz on 23.03.16.
 */
public interface ImpostorConfig {

    public String getJSon();

//    {
//        "mode" : "upstream",
//            "failuremode" : "none",
//            "protocol" : "http",
//            "server" : "localhost:8101",
//            "requests" : [
//        {
//            "number" : 1,
//                "verb" : "GET",
//                "resource" : "recommendation",
//                "params" : [
//            { "name" : "user", "value" : "U001"},
//            { "name" : "product", "value" : "P002"}
//            ]
//        },
//        {
//            "number" : 2,
//                "verb" : "GET",
//                "resource" : "recommendation",
//                "params" : [
//            { "name" : "user", "value" : "U005"},
//            { "name" : "product", "value" : "P001"}
//            ]
//        },
//        {
//            "number" : 3,
//                "verb" : "GET",
//                "resource" : "recommendation",
//                "params" : [
//            { "name" : "user", "value" : "U003"},
//            { "name" : "product", "value" : "P00T"}
//            ]
//        }
//        ]
//    }


}
