package com.baap.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String location;
    private String website;
    private String birthDate;
    private String email;
    private String password;
    private String mobile;
    private String image;
    private String video;
    private String backgroundImage;
    private String bio;
    private boolean reqUser;
    private boolean loginWithGoogle;

    @JsonIgnore
  

    @Embedded
    private Varification verification;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY) // Use LAZY fetch if needed
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY) // Use LAZY fetch if needed
    @JoinTable(
        name = "user_following",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<User> following = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<AssignmentSubmission> submissions;
    private boolean followed;
    private boolean verified;

	}
