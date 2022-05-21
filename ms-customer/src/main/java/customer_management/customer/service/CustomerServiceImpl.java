package customer_management.customer.service;

import customer_management.customer.model.CustomerDto;
import customer_management.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDto> getCustomers() {
        return customerRepository.selectAllCustomers();
    }

    @Override
    public CustomerDto getCustomer(Long id) {
        return customerRepository.selectCustomer(id);
    }

    @Override
    public CustomerDto addCustomer(CustomerDto customerDto) {
        return customerRepository.insertCustomer(customerDto);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        return customerRepository.updateCustomer(id, customerDto);
    }

    @Override
    public CustomerDto deleteCustomer(Long id) {
        return customerRepository.deleteCustomer(id);
    }
}