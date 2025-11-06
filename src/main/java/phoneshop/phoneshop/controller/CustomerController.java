package phoneshop.phoneshop.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;  // Import this
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import phoneshop.phoneshop.model.Customer;
import phoneshop.phoneshop.service.CustomerService;  // Ensure you're importing CustomerService

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  private final CustomerService service;

  // Manually add the constructor for dependency injection
  @Autowired  // Tells Spring to inject CustomerService
  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @GetMapping
  public List<Customer> list() { return service.list(); }

  @PostMapping
  public ResponseEntity<Customer> create(@Valid @RequestBody Customer c) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(c));
  }

  @GetMapping("/{id}")
  public Customer get(@PathVariable Long id) { return service.get(id); }

  @PutMapping("/{id}")
  public Customer update(@PathVariable Long id, @Valid @RequestBody Customer c) {
    return service.update(id, c);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) { service.delete(id); }
}
