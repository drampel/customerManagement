package customer_management.customer.util;

import customer_management.customer.exception.CustomerNotFoundException;
import customer_management.customer.mapper.CustomerMapper;
import customer_management.customer.model.CustomerDto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerUtil {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CustomerUtil(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public CustomerDto customerExistJdbcTemplate(Long id) {
        CustomerDto existingDto;
        try {
            existingDto = this.jdbcTemplate.queryForObject("SELECT c.id, c.name, c.surname, c.birth_date, " +
                    "c.phone, c.email, c.status, c.created_at, c.updated_at FROM customer AS c WHERE c.id = ? " +
                    "AND c.status != 'DELETED'", new CustomerMapper(), id);
        } catch (DataAccessException ex) {
            throw new CustomerNotFoundException(id);
        }
        return existingDto;
    }

    public CustomerDto customerExistNPJdbcTemplate(Long id) {
        CustomerDto existingDto;
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        try {
            existingDto = this.namedParameterJdbcTemplate.queryForObject("SELECT c.id, c.name, c.surname, c.birth_date, " +
                    "c.phone, c.email, c.status, c.created_at, c.updated_at FROM customer AS c WHERE c.id = :id AND " +
                    "c.status != 'DELETED'", namedParameters, new CustomerMapper());
        } catch (DataAccessException ex) {
            throw new CustomerNotFoundException(id);
        }
        return existingDto;
    }
}