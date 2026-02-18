package br.com.jurispay.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_profile_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfilePreferencesEntity {

    @Id
    @Column(name = "username", nullable = false, length = 120)
    private String username;

    @Column(name = "theme", nullable = false, length = 20)
    private String theme;

    @Column(name = "compact_mode", nullable = false)
    private Boolean compactMode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (theme == null) {
            theme = "SYSTEM";
        }
        if (compactMode == null) {
            compactMode = Boolean.FALSE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
        if (theme == null) {
            theme = "SYSTEM";
        }
        if (compactMode == null) {
            compactMode = Boolean.FALSE;
        }
    }
}
