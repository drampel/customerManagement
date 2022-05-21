package customer_management.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}