package com.github.torkjel.syshealth.worker.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.SneakyThrows;

@Builder
@Getter
public class Batch {

    private final static ObjectMapper OM = new ObjectMapper();

    private final String batchId;

    @Singular
    private List<Profile> profiles;

    @JsonCreator
    public Batch(
            @JsonProperty("batsh-id") String batchId,
            @JsonProperty("profiles") List<Profile> profiles) {
        this.batchId = batchId;
        this.profiles = profiles;
    }

    @SneakyThrows
    public static Batch parse(String json) {
        return OM.readerFor(Batch.class).readValue(json);
    }
}
