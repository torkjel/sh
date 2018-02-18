package com.github.torkjel.syshealth.worker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.torkjel.syshealth.worker.Main;
import com.github.torkjel.syshealth.worker.model.Batch;
import com.github.torkjel.syshealth.worker.model.BatchResponse;
import com.github.torkjel.syshealth.worker.model.HttpVerb;
import com.github.torkjel.syshealth.worker.model.Profile;
import com.github.torkjel.syshealth.worker.model.Status;
import com.github.torkjel.syshealth.worker.model.Target;
import com.github.torkjel.syshealth.worker.model.TargetType;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ProbeTargetTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    private Main m;

    @Before
    public void setUp() {
        m = new Main(new Wiring());
    }

    @Test
    public void testSuccess() {

        String url = "http://localhost:8089/ping";

        stubFor(get(urlEqualTo("/ping"))
                .willReturn(aResponse()
                    .withStatus(200)));

        BatchResponse response = m.process(
                Batch.builder()
                    .batchId("1")
                    .profile(Profile.builder()
                            .key("key")
                            .target(Target.builder()
                                    .type(TargetType.probe)
                                    .verb(HttpVerb.GET)
                                    .url(url)
                                    .build())
                            .build())
                    .build());

        assertThat(response.profile("key").target(url).success()).isTrue();
    }

    @Test
    public void testPost() {

        String url = "http://localhost:8089/ping";

        stubFor(post(urlEqualTo("/ping"))
                .willReturn(aResponse()
                    .withStatus(200)));

        BatchResponse response = m.process(
                Batch.builder()
                    .batchId("1")
                    .profile(Profile.builder()
                            .key("key")
                           .target(Target.builder()
                                   .type(TargetType.probe)
                                   .verb(HttpVerb.POST)
                                   .url(url)
                                   .build())
                            .build())
                    .build());

        assertThat(response.profile("key").target(url).success());
    }

    @Test
    public void testFollowRedirects() {

        String url = "http://localhost:8089/ping2";

        stubFor(get("/ping2")
                .willReturn(temporaryRedirect("/pingV2")));

        stubFor(get(urlEqualTo("/pingV2"))
                .willReturn(aResponse()
                    .withStatus(202)));

        BatchResponse response = m.process(
                Batch.builder()
                    .batchId("1")
                    .profile(Profile.builder()
                            .key("key")
                           .target(Target.builder()
                                   .type(TargetType.probe)
                                   .verb(HttpVerb.GET)
                                   .url(url)
                                   .followRedirects(true)
                                   .build())
                            .build())
                    .build());

        Status status = response.profile("key").target(url).getStatus();
        assertThat(status.success());
        assertThat(status.getHttpStatusCode()).isEqualTo(202);
        assertThat(status.getHttpStatusReason()).isEqualTo("Accepted");
    }

    @Test
    public void testHeaders() {

        String url = "http://localhost:8089/ping";

        stubFor(get("/ping").withHeader("Accept", equalTo("foo/bar"))
                .willReturn(aResponse()
                    .withStatus(202)));

        stubFor(get("/ping").withHeader("Accept", equalTo("foo/zoo"))
                .willReturn(aResponse()
                    .withStatus(200)));

        BatchResponse response = m.process(
                Batch.builder()
                    .batchId("1")
                    .profile(Profile.builder()
                            .key("key")
                           .target(Target.builder()
                                   .type(TargetType.probe)
                                   .verb(HttpVerb.GET)
                                   .url(url)
                                   .headers(Arrays.<String[]>asList(new String[] {"Accept", "foo/bar"}))
                                   .build())
                            .build())
                    .build());

        Status status = response.profile("key").target(url).getStatus();
        assertThat(status.success()).isTrue();
        assertThat(status.getCode()).isEqualTo(Status.Code.SUCCESS);
        assertThat(status.getHttpStatusCode()).isEqualTo(202);
        assertThat(status.getHttpStatusReason()).isEqualTo("Accepted");
    }

    @Test
    public void testNotFound() {

        String url = "http://localhost:8089/ping";

        stubFor(get(urlEqualTo("/ping"))
                .willReturn(aResponse()
                    .withStatus(404)));

        BatchResponse response = m.process(
                Batch.builder()
                    .batchId("1")
                    .profile(Profile.builder()
                            .key("key")
                           .target(Target.builder()
                                   .type(TargetType.probe)
                                   .verb(HttpVerb.GET)
                                   .url(url)
                                   .build())
                            .build())
                    .build());

        Status status = response.profile("key").target(url).getStatus();
        assertThat(status.success()).isFalse();
        assertThat(status.getCode()).isEqualTo(Status.Code.FAILED);
        assertThat(status.getHttpStatusCode()).isEqualTo(404);
        assertThat(status.getHttpStatusReason()).isEqualTo("Not Found");
    }

    @Test
    public void testTimeout() {

        String url = "http://localhost:8089/ping";

        stubFor(get(urlEqualTo("/ping"))
                .willReturn(aResponse()
                    .withStatus(404)
                    .withFixedDelay(3000)));

        long time = System.currentTimeMillis();
        BatchResponse response = m.process(
                Batch.builder()
                    .batchId("1")
                    .profile(Profile.builder()
                            .key("key")
                            .target(Target.builder()
                                    .type(TargetType.probe)
                                    .verb(HttpVerb.GET)
                                    .url(url)
                                    .timeout(2000)
                                    .build())
                            .build())
                    .build());
        time = System.currentTimeMillis() - time;

        Status status = response.profile("key").target(url).getStatus();
        assertThat(status.getTime()).isGreaterThan(2000);
        assertThat(status.getCode()).isEqualTo(Status.Code.TIMEDOUT);
        assertThat(status.getHttpStatusCode()).isEqualTo(0);
        assertThat(status.getHttpStatusReason()).isEqualTo("timed out");
        assertThat(time).isGreaterThan(2000);
    }
}
