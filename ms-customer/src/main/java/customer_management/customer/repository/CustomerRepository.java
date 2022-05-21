package customer_management.customer.repository;

import customer_management.customer.model.CustomerDto;

import java.util.List;

public interface CustomerRepository {
    List<CustomerDto> selectAllCustomers();

    CustomerDto selectCustomer(Long id);

    CustomerDto insertCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    CustomerDto deleteCustomer(Long id);
}