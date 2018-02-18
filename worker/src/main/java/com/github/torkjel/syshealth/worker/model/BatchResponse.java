package com.github.torkjel.syshealth.worker.model;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BatchResponse {
    private final String batchId;

    @Singular
    private List<ProfileResponse> profiles;

    @SneakyThrows
    public String toJson() {
        return new ObjectMapper().writer().writeValueAsString(this);
    }

    public ProfileResponse profile(String profileKey) {
        return profiles.stream()
                .filter(pr -> pr.getKey().equals(profileKey))
                .findFirst()
                .orElse(null);
    }
}
