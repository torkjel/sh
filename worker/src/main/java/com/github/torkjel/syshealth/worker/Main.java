package com.github.torkjel.syshealth.worker;

import static spark.Spark.*;

import org.apache.log4j.Logger;

import com.github.torkjel.syshealth.worker.model.Batch;
import com.github.torkjel.syshealth.worker.model.BatchResponse;

import lombok.Getter;
import spark.Request;
import spark.Response;
import spark.Spark;

@Getter
public class Main {

    private final static Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        new Main(new Wiring()).go();
    }

    private final Wiring w;

    public Main(Wiring w) {
        this.w = w;
    }

    public void go() {
        port(w.getPort());
        post("/batch", this::processBatch);
        Spark.awaitInitialization();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Going down.");
        }));
        LOG.info("We're up!");
    }

    public String processBatch(Request req, Response res) {
        String response = process(Batch.parse(req.body())).toJson();
        res.type("application/json");
        return response;
    }

    public BatchResponse process(Batch batch) {
        return new Dispatcher(w.getTargetProcessingService(), batch).process();
    }
}












