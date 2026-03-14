package nitkkr.Backend.model;

import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;
    private String title;

    @Column(length = 1000)
    private String review;

    // getters setters
}
