package com.weekly_projects.OrderAPI.OrderAPI.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedOrderEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class CreatedOrderEventContractTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void createdOrderEvent_serializesWithExpectedFields() throws Exception {
        CreatedOrderEvent event = new CreatedOrderEvent();
        event.setEventId("event-123");
        event.setEventVersion(1);
        event.setOccurredAt(Instant.parse("2026-07-25T03:48:48Z"));
        event.setOrderId(100L);
        event.setOrderDate(Instant.parse("2026-07-25T03:48:48Z"));
        event.setCustomerId(1L);

        String json = objectMapper.writeValueAsString(event);

        JsonNode root = objectMapper.readTree(json);

        assertTrue(root.has("eventId"));
        assertTrue(root.has("eventVersion"));
        assertTrue(root.has("occurredAt"));
        assertTrue(root.has("orderId"));
        assertTrue(root.has("orderDate"));
        assertTrue(root.has("customerId"));

        assertEquals("event-123", root.get("eventId").asText());
        assertEquals(1, root.get("eventVersion").asInt());
        assertEquals("2026-07-25T03:48:48Z", root.get("occurredAt").asText());
        assertEquals(100L, root.get("orderId").asLong());
        assertEquals("2026-07-25T03:48:48Z", root.get("orderDate").asText());
        assertEquals(1L, root.get("customerId").asLong());
    }

    @Test
    void createdOrderEvent_deserializesWithExpectedFields() throws Exception {
        String json = """
                {
                  "eventId": "event-123",
                  "eventVersion": 1,
                  "occurredAt": "2026-07-25T03:48:48Z",
                  "orderId": 100,
                  "orderDate": "2026-07-25T03:48:48Z",
                  "customerId": 1
                }
                """;

        CreatedOrderEvent event = objectMapper.readValue(json, CreatedOrderEvent.class);

        assertEquals("event-123", event.getEventId());
        assertEquals(1, event.getEventVersion());
        assertEquals(Instant.parse("2026-07-25T03:48:48Z"), event.getOccurredAt());
        assertEquals(100L, event.getOrderId());
        assertEquals(Instant.parse("2026-07-25T03:48:48Z"), event.getOrderDate());
        assertEquals(1L, event.getCustomerId());
    }
}
