package phoneshop.phoneshop.service;

import org.springframework.stereotype.Service;
import phoneshop.phoneshop.exception.NotFoundException;
import phoneshop.phoneshop.model.Customer;
import phoneshop.phoneshop.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {

  private final CustomerRepository repo;

  // Constructor for dependency injection
  public CustomerService(CustomerRepository repo) {
    this.repo = repo;
  }

  public Customer create(Customer c) {
    return repo.save(c);
  }

  public List<Customer> list() {
    return repo.findAll();
  }

  public Customer get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found: " + id));
  }

  public Customer update(Long id, Customer data) {
    Customer c = get(id);
    c.setFirstName(data.getFirstName());  // Calling the manually written getter
    c.setLastName(data.getLastName());    // Calling the manually written getter
    c.setEmail(data.getEmail());
    c.setPhoneNumber(data.getPhoneNumber());
    return repo.save(c);
  }

  public void delete(Long id) {
    repo.delete(get(id));
  }
}
