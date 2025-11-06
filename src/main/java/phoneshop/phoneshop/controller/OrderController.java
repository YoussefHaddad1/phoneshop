package phoneshop.phoneshop.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;  // Import this annotation
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import phoneshop.phoneshop.dto.CreateOrderRequest;
import phoneshop.phoneshop.model.Order;
import phoneshop.phoneshop.service.OrderService;  // Import the OrderService

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService service;

  // Manually add the constructor for dependency injection
  @Autowired  // Tells Spring to inject OrderService
  public OrderController(OrderService service) {
    this.service = service;
  }

  @GetMapping
  public List<Order> list() { return service.list(); }

  @GetMapping("/{id}")
  public Order get(@PathVariable Long id) { return service.get(id); }

  @PostMapping
  public ResponseEntity<Order> create(@Valid @RequestBody CreateOrderRequest req) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) { service.delete(id); }
}
