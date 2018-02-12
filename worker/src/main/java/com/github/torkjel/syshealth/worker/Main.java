package com.github.torkjel.syshealth.worker;

import static spark.Spark.*;

import com.github.torkjel.syshealth.worker.model.Batch;
import com.github.torkjel.syshealth.worker.model.BatchResponse;
import com.github.torkjel.syshealth.worker.model.TargetType;

import spark.Request;
import spark.Response;
import spark.Spark;

import static org.asynchttpclient.Dsl.*;

public class Main {

    static TargetProcessor targetProcessor;

    static {
        targetProcessor = TargetProcessingService.builder()
                .processor(
                        TargetType.URL_GET,
                        new ProbeProcessor(
                                new ProbeResponseParser(),
                                asyncHttpClient(config())))
                .build();
    }

    public static void main(String[] args) {
        try {
            new Main(9080, targetProcessor).go();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private final int port;
    private final TargetProcessor processor;

    public Main(int port, TargetProcessor procesor) {
        this.port = port;
        this.processor = procesor;
    }

    private void go() {
        port(port);
        get("/hello", (req, res) -> "Hello world");
        post("/batch", this::processBatch);
        Spark.awaitInitialization();
        System.out.println("Foo");
    }

    private String processBatch(Request req, Response res) {
        String response = new Dispatcher(processor, Batch.parse(req.body())).process().toJson();
        res.type("application/json");
        return response;
    }
}












