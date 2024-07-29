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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
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
            System.out.println("Минимальная продолжительность полета: ");
            Map<String, LocalTime> minTimes = minFlightTimePerCarrier(tickets);
            minTimes.forEach((carrier, time) -> System.out.println(carrier + ": " + time));
            System.out.println("Разница между средней ценой и медианой: "+ calculateMedianAverageDifference(tickets.getTickets()));
        }
    }

    public static Map<String, LocalTime> minFlightTimePerCarrier(TicketsDTO tickets) {
        return tickets.getTickets().stream()
                // Фильтруем билеты по указанным городам
                .filter(ticket -> "VVO".equals(ticket.getOrigin()) && "TLV".equals(ticket.getDestination()))
                .collect(Collectors.groupingBy(TicketDTO::getCarrier,
                        Collector.of(
                                () -> new ArrayList<Duration>(),
                                (list, ticket) -> {
                                    LocalDateTime departureDateTime = LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime());
                                    LocalDateTime arrivalDateTime = LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime());
                                    list.add(Duration.between(departureDateTime, arrivalDateTime));
                                },
                                (list1, list2) -> { list1.addAll(list2); return list1; },
                                list -> list.stream()
                                        .min(Comparator.naturalOrder())
                                        .map(duration -> LocalTime.of((int) duration.toHours(), duration.toMinutesPart()))
                                        .orElse(null)
                        )
                ));
    }




    public static double calculateMedianAverageDifference(List<TicketDTO> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("Список билетов не должен быть пустым");
        }

        // Фильтруем билеты по маршруту Владивосток - Тель-Авив
        List<Integer> prices = tickets.stream()
                .filter(ticket -> "VVO".equals(ticket.getOrigin()) && "TLV".equals(ticket.getDestination()))
                .map(TicketDTO::getPrice)
                .collect(Collectors.toList());

        if (prices.isEmpty()) {
            throw new IllegalArgumentException("Нет билетов по указанному маршруту");
        }

        // Вычисление средней цены
        double averagePrice = prices.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(Double.NaN);

        // Вычисление медианы
        List<Integer> sortedPrices = prices.stream()
                .sorted()
                .collect(Collectors.toList());

        double median;
        int size = sortedPrices.size();
        if (size % 2 == 0) { //Если количество четное
            median = (sortedPrices.get(size / 2 - 1) + sortedPrices.get(size / 2)) / 2.0;
        } else {
            median = sortedPrices.get(size / 2);
        }

        // Разница между медианой и средней ценой
        return median - averagePrice;
    }
}
