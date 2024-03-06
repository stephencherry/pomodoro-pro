package com.platform.pomodoropro.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String firstname;
    private String lastname;
    private String password;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ROLE role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private STATUS status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'NEUTRAL'")
    private GENDER gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserEntity userEntity;

}
