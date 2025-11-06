package phoneshop.phoneshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "phones")
public class Phone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String model;

  @Positive
  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal price;

  @Min(0)
  private Integer stock;

  @Column(length = 2000)
  private String specs;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id", nullable = false)
  private Brand brand;

  // Default constructor
  public Phone() {}

  // Constructor with fields
  public Phone(Long id, String model, BigDecimal price, Integer stock, String specs, Brand brand) {
    this.id = id;
    this.model = model;
    this.price = price;
    this.stock = stock;
    this.specs = specs;
    this.brand = brand;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public String getSpecs() {
    return specs;
  }

  public void setSpecs(String specs) {
    this.specs = specs;
  }

  public Brand getBrand() {
    return brand;
  }

  public void setBrand(Brand brand) {
    this.brand = brand;
  }
}
