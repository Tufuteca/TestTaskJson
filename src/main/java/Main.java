import DTO.TicketDTO;
import DTO.TicketsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TicketsDTO tickets = null;
        try {
            tickets = objectMapper.readValue(new File("src/main/resources/JSON/tickets.json"), TicketsDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tickets != null) {
            System.out.println("Минимальная продолжительность полета: " + minFlightTime(tickets));
            System.out.println("Разница между средней ценой и медианой: "+ calculateMedianAverageDifference(tickets.getTickets()));
        }
    }

    }
}
