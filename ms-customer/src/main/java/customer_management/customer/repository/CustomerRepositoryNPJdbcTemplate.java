package customer_management.customer.repository;

import customer_management.customer.mapper.CustomerMapper;
import customer_management.customer.model.CustomerDto;
import customer_management.customer.model.Status;
import customer_management.customer.util.CustomerUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("dev")
public class CustomerRepositoryNPJdbcTemplate implements CustomerRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final CustomerUtil customerUtil;

    public CustomerRepositoryNPJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                            CustomerUtil customerUtil) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.customerUtil = customerUtil;
    }

    @Override
    public List<CustomerDto> selectAllCustomers() {
        List<CustomerDto> customers = this.namedParameterJdbcTemplate.query("SELECT c.id, c.name, c.surname, " +
                        "c.birth_date, c.phone, c.email, c.status, c.created_at, c.updated_at FROM customer AS c " +
                        "WHERE c.status!= 'DELETED' ORDER BY c.id",
                new CustomerMapper());
        return customers.stream().map(CustomerMapper::toDto).toList();
    }

    @Override
    public CustomerDto selectCustomer(Long id) {
        return CustomerMapper.toDto(customerUtil.customerExistNPJdbcTemplate(id));
    }

    @Override
    public CustomerDto insertCustomer(CustomerDto customerDto) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("name", customerDto.getName());
        namedParameters.addValue("surname", customerDto.getSurname());
        namedParameters.addValue("birth_date", customerDto.getBirthDate());
        namedParameters.addValue("phone", customerDto.getPhone());
        namedParameters.addValue("email", customerDto.getEmail());
        this.namedParameterJdbcTemplate.update("INSERT INTO customer(\"name\", \"surname\", \"birth_date\", " +
                "\"phone\", \"email\") VALUES (:name, :surname, :birth_date, :phone, :email)", namedParameters);
        CustomerDto createdDto = this.namedParameterJdbcTemplate.queryForObject("SELECT c.id, c.name, c.surname, " +
                "c.birth_date, c.phone, c.email, c.status, c.created_at, c.updated_at FROM customer AS c " +
                "WHERE c.email = :email AND c.status != 'DELETED'", namedParameters, new CustomerMapper());
        if (createdDto != null) {
            return CustomerMapper.toDto(createdDto);
        } else return null;
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        CustomerDto existingDto = customerUtil.customerExistNPJdbcTemplate(id);
        CustomerMapper.toExistingDto(existingDto, customerDto);
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", existingDto.getId());
        namedParameters.addValue("name", existingDto.getName());
        namedParameters.addValue("surname", existingDto.getSurname());
        namedParameters.addValue("birth_date", existingDto.getBirthDate());
        namedParameters.addValue("phone", existingDto.getPhone());
        namedParameters.addValue("email", existingDto.getEmail());
        namedParameters.addValue("status", existingDto.getStatus().name());
        namedParameters.addValue("updated_at", existingDto.getUpdatedAt());
        this.namedParameterJdbcTemplate.update("UPDATE customer AS c SET name = :name, surname = :surname, " +
                "birth_date = :birth_date, phone = :phone, email = :email, status = :status::customer_status, " +
                "updated_at = :updated_at WHERE c.id = :id", namedParameters);
        return CustomerMapper.toDto(existingDto);
    }

    @Override
    public CustomerDto deleteCustomer(Long id) {
        CustomerDto deletedCustomer = customerUtil.customerExistNPJdbcTemplate(id);
        deletedCustomer.setStatus(Status.DELETED);
        deletedCustomer.setUpdatedAt(LocalDateTime.now());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", deletedCustomer.getId());
        namedParameters.addValue("status", deletedCustomer.getStatus().name());
        namedParameters.addValue("updated_at", deletedCustomer.getUpdatedAt());
        this.namedParameterJdbcTemplate.update("UPDATE customer AS c SET status = CAST(:status AS customer_status), " +
                "updated_at = :updated_at WHERE c.id = :id", namedParameters);
        return CustomerMapper.toDto(deletedCustomer);
    }
}