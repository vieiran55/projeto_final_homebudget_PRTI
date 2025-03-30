package br.com.homebudget.users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name", nullable = false, length = 255)
    String name;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    String email;

    @Column(name = "password", length = 255)
    String password;

    @Column(name = "remember_token", length = 255)
    String rememberToken;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    Timestamp updatedAt;

    @UpdateTimestamp
    @Column(name = "deleted_at")
    Timestamp deletedAt;
}
