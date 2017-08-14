package co.com.fixitgroup.service;


import co.com.fixitgroup.domain.Customer;
import co.com.fixitgroup.domain.User;
import co.com.fixitgroup.repository.AuthorityRepository;
import co.com.fixitgroup.repository.CustomerRepository;
import co.com.fixitgroup.service.dto.CustomerDTO;
import co.com.fixitgroup.service.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private CustomerRepository customerRepository;

    private UserService userService;

    private final CustomerMapper customerMapper;

    private final AuthorityRepository authorityRepository;

    public CustomerService(CustomerRepository customerRepository, UserService userService, CustomerMapper customerMapper, AuthorityRepository authorityRepository) {
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.customerMapper = customerMapper;
        this.authorityRepository = authorityRepository;
    }


    public Customer createCustomer(CustomerDTO customerDto) {
        customerDto.getUser().getAuthorities().add("ROLE_USER");
        Customer customer = customerMapper.customerFromDto(customerDto);
        User user = userService.createUser(
            customerDto.getUser().getLogin(),
            customerDto.getUser().getPassword(),
            customerDto.getUser().getFirstName(),
            customerDto.getUser().getLastName(),
            customerDto.getUser().getEmail(),
            customerDto.getUser().getImageUrl(),
            customerDto.getUser().getLangKey()
        );
        customer.setUser(user);
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

}
