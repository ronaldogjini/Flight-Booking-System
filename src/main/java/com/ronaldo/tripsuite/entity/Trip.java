package com.ronaldo.tripsuite.entity;

import com.ronaldo.tripsuite.enums.TripReason;
import com.ronaldo.tripsuite.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private TripReason reason;

    @Column(name = "description")
    private String description;

    @Column(name = "departure_location")
    private String departureLocation;

    @Column(name = "arrival_location")
    private String arrivalLocation;


    @Column(name = "departure_date")
    private Date departureDate;

    @Column(name = "arrival_date")
    private Date arrivalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TripStatus status;

    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "trip_flights",
            joinColumns = {
                    @JoinColumn(name = "trip_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "flight_schedule_id")
            }
    )
    private List<FlightSchedule> flightSchedules;

}
