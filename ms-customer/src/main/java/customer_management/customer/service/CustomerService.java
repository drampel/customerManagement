package customer_management.customer.service;

import customer_management.customer.model.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getCustomers();

    CustomerDto getCustomer(Long id);

    CustomerDto addCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    CustomerDto deleteCustomer(Long id);
}