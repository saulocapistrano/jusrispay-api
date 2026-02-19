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
import java.time.LocalTime;

@Entity
@Table(name = "system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfigEntity {

    @Id
    private Long id;

    @Column(name = "brand_name", length = 120)
    private String brandName;

    @Column(name = "contact_email", length = 180)
    private String contactEmail;

    @Column(name = "contact_phone", length = 40)
    private String contactPhone;

    @Column(name = "cnpj", length = 30)
    private String cnpj;

    @Column(name = "pix_key", length = 255)
    private String pixKey;

    @Column(name = "notification_timezone", length = 80, nullable = false)
    private String notificationTimezone;

    @Column(name = "reminder_dispatch_time", nullable = false)
    private LocalTime reminderDispatchTime;

    @Column(name = "collection_dispatch_time", nullable = false)
    private LocalTime collectionDispatchTime;

    @Column(name = "logo_original_file_name", length = 255)
    private String logoOriginalFileName;

    @Column(name = "logo_content_type", length = 120)
    private String logoContentType;

    @Column(name = "logo_size_bytes")
    private Long logoSizeBytes;

    @Column(name = "logo_bucket", length = 120)
    private String logoBucket;

    @Column(name = "logo_object_key", length = 255)
    private String logoObjectKey;

    @Column(name = "created_at", nullable = false)
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

        if (notificationTimezone == null || notificationTimezone.isBlank()) {
            notificationTimezone = "America/Fortaleza";
        }
        if (reminderDispatchTime == null) {
            reminderDispatchTime = LocalTime.of(9, 0);
        }
        if (collectionDispatchTime == null) {
            collectionDispatchTime = LocalTime.of(10, 0);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
