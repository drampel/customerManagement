package customer_management.customer.mapper;

import customer_management.customer.model.CustomerDto;
import customer_management.customer.model.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CustomerMapper implements RowMapper<CustomerDto> {
    @Override
    public CustomerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return CustomerDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .birthDate(rs.getDate("birth_date").toLocalDate())
                .phone(rs.getString("phone"))
                .email(rs.getString("email"))
                .status(Status.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

    public static CustomerDto toDto(CustomerDto dto) {
        return CustomerDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .birthDate(dto.getBirthDate())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static void toExistingDto(CustomerDto existingDto, CustomerDto customerDto) {
        if (customerDto.getName() != null) existingDto.setName(customerDto.getName());
        if (customerDto.getSurname() != null) existingDto.setSurname(customerDto.getSurname());
        if (customerDto.getBirthDate() != null) existingDto.setBirthDate(customerDto.getBirthDate());
        if (customerDto.getPhone() != null) existingDto.setPhone(customerDto.getPhone());
        if (customerDto.getEmail() != null) existingDto.setEmail(customerDto.getEmail());
        if (customerDto.getStatus() != null && !customerDto.getStatus().name().equals("DELETED"))
            existingDto.setStatus(customerDto.getStatus());
        existingDto.setUpdatedAt(LocalDateTime.now());
    }
}