package customer_management.customer.repository;

import customer_management.customer.mapper.CustomerMapper;
import customer_management.customer.model.CustomerDto;
import customer_management.customer.model.Status;
import customer_management.customer.util.CustomerUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("default")
public class CustomerRepositoryJdbcTemplate implements CustomerRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerUtil customerUtil;

    public CustomerRepositoryJdbcTemplate(JdbcTemplate jdbcTemplate,
                                          CustomerUtil customerUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerUtil = customerUtil;
    }

    @Override
    public List<CustomerDto> selectAllCustomers() {
        List<CustomerDto> customers = this.jdbcTemplate.query("SELECT c.id, c.name, c.surname, c.birth_date, " +
                "c.phone, c.email, c.status, c.created_at, c.updated_at FROM customer AS c WHERE " +
                "c.status!= 'DELETED' ORDER BY c.id", new CustomerMapper());
        return customers.stream().map(CustomerMapper::toDto).toList();
    }

    @Override
    public CustomerDto selectCustomer(Long id) {
        return CustomerMapper.toDto(customerUtil.customerExistJdbcTemplate(id));
    }

    @Override
    public CustomerDto insertCustomer(CustomerDto customerDto) {
        this.jdbcTemplate.update("INSERT INTO customer(\"name\", \"surname\", \"birth_date\", \"phone\", \"email\") " +
                        "VALUES (?,?,?,?,?)", customerDto.getName(), customerDto.getSurname(), customerDto.getBirthDate(),
                customerDto.getPhone(), customerDto.getEmail());
        CustomerDto createdDto = this.jdbcTemplate.queryForObject("SELECT c.id, c.name, c.surname, c.birth_date, " +
                        "c.phone, c.email, c.status, c.created_at, c.updated_at FROM customer AS c WHERE c.email = ?",
                new CustomerMapper(), customerDto.getEmail());
        if (createdDto != null) {
            return CustomerMapper.toDto(createdDto);
        } else return null;
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        CustomerDto existingDto = customerUtil.customerExistJdbcTemplate(id);
        CustomerMapper.toExistingDto(existingDto, customerDto);
        this.jdbcTemplate.update("UPDATE customer AS c SET name = ?, surname = ?, birth_date = ?, phone = ?, " +
                        "email = ?, status = ?::customer_status, updated_at = ? where c.id = ?", existingDto.getName(),
                existingDto.getSurname(), existingDto.getBirthDate(), existingDto.getPhone(), existingDto.getEmail(),
                existingDto.getStatus().name(), existingDto.getUpdatedAt(), id);
        return CustomerMapper.toDto(existingDto);
    }

    @Override
    public CustomerDto deleteCustomer(Long id) {
        CustomerDto deletedCustomer = customerUtil.customerExistJdbcTemplate(id);
        deletedCustomer.setStatus(Status.DELETED);
        deletedCustomer.setUpdatedAt(LocalDateTime.now());
        this.jdbcTemplate.update("UPDATE customer AS c SET status = CAST(? AS customer_status), " +
                "updated_at = ? where c.id = ?", deletedCustomer.getStatus().name(), deletedCustomer.getUpdatedAt(), id);
        return CustomerMapper.toDto(deletedCustomer);
    }
}