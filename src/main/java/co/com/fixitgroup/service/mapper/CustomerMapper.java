package co.com.fixitgroup.service.mapper;

import co.com.fixitgroup.domain.Customer;
import co.com.fixitgroup.domain.User;
import co.com.fixitgroup.service.dto.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private final UserMapper userMapper;

    public CustomerMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public Customer customerFromDto(CustomerDTO customerDto) {
        User user = userMapper.userDTOToUser(customerDto.getUser());
        Customer customer = new Customer();
        customer.setPhone(customerDto.getPhone());
        return customer;
    }
}
