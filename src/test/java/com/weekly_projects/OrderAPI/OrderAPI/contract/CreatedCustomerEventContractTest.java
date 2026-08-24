package com.weekly_projects.OrderAPI.OrderAPI.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedCustomerEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class CreatedCustomerEventContractTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void createdCustomerEvent_serializesWithExpectedFields() throws Exception {
        CreatedCustomerEvent event = new CreatedCustomerEvent();
        event.setEventId("event-123");
        event.setEventVersion(1);
        event.setOccurredAt(Instant.parse("2026-05-25T03:50:50Z"));
        event.setCustomerId(100L);
        event.setEmail("joe.doe@gmail.com");
        event.setName("Joe Doe");

        String json = objectMapper.writeValueAsString(event);

        JsonNode root = objectMapper.readTree(json);

        assertTrue(root.has("eventId"));
        assertTrue(root.has("eventVersion"));
        assertTrue(root.has("occurredAt"));
        assertTrue(root.has("customerId"));
        assertTrue(root.has("email"));
        assertTrue(root.has("name"));

        assertEquals("event-123", root.get("eventId").asText());
        assertEquals(1, root.get("eventVersion").asInt());
        assertEquals("2026-05-25T03:50:50Z",  root.get("occurredAt").asText());
        assertEquals(100L, root.get("customerId").asLong());
        assertEquals("joe.doe@gmail.com", root.get("email").asText() );
        assertEquals("Joe Doe", root.get("name").asText());
    }

    @Test
    void createdCustomerEvent_deserializesWithExpectedFields() throws Exception {
        String json = """
                {
                "eventId": "event-123",
                "eventVersion": 1,
                "occurredAt": "2026-05-25T03:50:50Z",
                "customerId": 100,
                "email": "joe.doe@gmail.com",
                "name": "Joe Doe"
                }
                """;

        CreatedCustomerEvent event = objectMapper.readValue(json, CreatedCustomerEvent.class);

        assertEquals("event-123", event.getEventId());
        assertEquals(1, event.getEventVersion());
        assertEquals(Instant.parse("2026-05-25T03:50:50Z"),  event.getOccurredAt());
        assertEquals(100L, event.getCustomerId());
        assertEquals("joe.doe@gmail.com", event.getEmail());
        assertEquals("Joe Doe", event.getName());
    }
}