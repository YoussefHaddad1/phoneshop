package phoneshop.phoneshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phoneshop.phoneshop.exception.NotFoundException;
import phoneshop.phoneshop.model.Order;
import phoneshop.phoneshop.model.Phone;
import phoneshop.phoneshop.model.Customer;
import phoneshop.phoneshop.model.OrderLine;  // <-- Make sure this is imported
import phoneshop.phoneshop.repository.OrderRepository;
import phoneshop.phoneshop.repository.PhoneRepository;
import phoneshop.phoneshop.repository.CustomerRepository;
import phoneshop.phoneshop.dto.CreateOrderRequest;
import phoneshop.phoneshop.dto.OrderItemRequest;
import phoneshop.phoneshop.exception.BadRequestException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

  private final OrderRepository orderRepo;
  private final PhoneRepository phoneRepo;
  private final CustomerRepository customerRepo;

  // Constructor for dependency injection
  @Autowired
  public OrderService(OrderRepository orderRepo, PhoneRepository phoneRepo, CustomerRepository customerRepo) {
    this.orderRepo = orderRepo;
    this.phoneRepo = phoneRepo;
    this.customerRepo = customerRepo;
  }

  public Order create(CreateOrderRequest req) {
    Customer customer = customerRepo.findById(req.customerId())
            .orElseThrow(() -> new NotFoundException("Customer not found: " + req.customerId()));

    if (req.items() == null || req.items().isEmpty())
      throw new BadRequestException("Order must contain at least one item");

    // Manually creating the Order using the constructor
    Order order = new Order(null, customer, LocalDateTime.now(), "NEW", BigDecimal.ZERO);

    order = orderRepo.save(order);

    BigDecimal total = BigDecimal.ZERO;

    for (OrderItemRequest item : req.items()) {
      Phone phone = phoneRepo.findById(item.phoneId())
              .orElseThrow(() -> new NotFoundException("Phone not found: " + item.phoneId()));

      if (phone.getStock() < item.quantity()) {
        throw new BadRequestException("Insufficient stock for phone id " + phone.getId());
      }

      // Decrement stock
      phone.setStock(phone.getStock() - item.quantity());
      phoneRepo.save(phone);

      // Create the OrderLine and save it
      OrderLine line = new OrderLine();
      line.setOrder(order);
      line.setPhone(phone);
      line.setQuantity(item.quantity());
      line.setUnitPrice(phone.getPrice());

      order.addLine(line); // Add line to the order
      total = total.add(phone.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
    }

    order.setTotal(total);
    return orderRepo.save(order);
  }

  public List<Order> list() {
    return orderRepo.findAll();
  }

  public Order get(Long id) {
    return orderRepo.findById(id).orElseThrow(() -> new NotFoundException("Order not found: " + id));
  }

  public void delete(Long id) {
    orderRepo.delete(get(id));
  }
}
