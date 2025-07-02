package dev.rohitahuja.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class TimeFunction implements
        Function<TimeFunction.Request, TimeFunction.Response> {

    @JsonClassDescription("Request to get time by zone id")
    public record Request(@JsonProperty(required = true, value = "zoneId")
                              @JsonPropertyDescription("Time zone id, such as Asia/Shanghai") String zoneId) {
    }

    @JsonClassDescription("Response to get time by zone id")
    public record Response(@JsonPropertyDescription("time") String time) {
    }

    @Override
    public Response apply(Request request) {
        ZoneId zid = ZoneId.of(request.zoneId());
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zid);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return new Response(zonedDateTime.format(formatter));
    }
}