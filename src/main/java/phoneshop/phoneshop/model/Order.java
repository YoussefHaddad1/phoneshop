package phoneshop.phoneshop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderLine> lines = new ArrayList<>();

  @Column(nullable = false)
  private LocalDateTime orderDate;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal total;

  // Default constructor
  public Order() {}

  // Constructor for setting fields
  public Order(Long id, Customer customer, LocalDateTime orderDate, String status, BigDecimal total) {
    this.id = id;
    this.customer = customer;
    this.orderDate = orderDate;
    this.status = status;
    this.total = total;
    this.lines = new ArrayList<>();  // Initialize lines list
  }

  // Getter and Setter for 'id'
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  // Getter and Setter for 'customer'
  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  // Getter and Setter for 'lines'
  public List<OrderLine> getLines() {
    return lines;
  }

  public void setLines(List<OrderLine> lines) {
    this.lines = lines;
  }

  // Getter and Setter for 'orderDate'
  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  // Getter and Setter for 'status'
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  // Getter and Setter for 'total'
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  // Method to add an order line
  public void addLine(OrderLine line) {
    line.setOrder(this);
    this.lines.add(line);
  }
}
