package com.lodekennes.carrental.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lodekennes.carrental.models.Reservation;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SetReservationSerializer extends JsonSerializer<Set<Reservation>> {
    @Override
    public void serialize(Set<Reservation> reservationSet, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Integer> ids = reservationSet.stream().map(Reservation::getId).map(Integer::intValue).collect(Collectors.toList());

        jsonGenerator.writeStartArray(ids.size());
        for(Integer id : ids) {
            jsonGenerator.writeNumber(id);
        }
        jsonGenerator.writeEndArray();
    }
}
