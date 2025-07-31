package com.project.domain.entity;

import com.project.interfaces.ConcertRepositoryInterface;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "concert")
public class Concert {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId;
    private String name;
    private LocalDate date;
    private Long venueId;

    protected Concert() {}

    public Concert(String name, LocalDate date, Long venueId) {
        this.name = name;
        this.date = date;
        this.venueId = venueId;
    }


    public Long getConcertId() {
        return concertId;
    }
}
