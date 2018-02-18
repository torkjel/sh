package com.github.torkjel.syshealth.worker.model;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ProfileResponse {
    private final String key;
    @Singular
    private List<TargetResult> results;

    public TargetResult target(String url) {
        return results.stream()
                .filter(tr -> tr.getUrl().equals(url))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends TargetResult> T targetAs(String url) {
        return (T)target(url);
    }
}
