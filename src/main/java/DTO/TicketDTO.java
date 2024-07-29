package DTO;

import Util.CustomLocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TicketDTO {
    @JsonProperty("origin")
    private String origin;

    @JsonProperty("origin_name")
    private String originName;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("destination_name")
    private String destinationName;

    @JsonProperty("departure_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate departureDate;

    @JsonProperty("departure_time")
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime departureTime;

    @JsonProperty("arrival_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate arrivalDate;

    @JsonProperty("arrival_time")
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime arrivalTime;

    @JsonProperty("carrier")
    private String carrier;

    @JsonProperty("stops")
    private int stops;

    @JsonProperty("price")
    private int price;

    public TicketDTO(int price) {
        this.price = price;
    }
}
