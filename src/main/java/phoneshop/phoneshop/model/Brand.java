package phoneshop.phoneshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "brands")
public class Brand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String name;

  // Default constructor
  public Brand() {}

  // Constructor for setting fields
  public Brand(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  // Getter and Setter for 'id'
  public Long getId() {
    return id;id
  }

  public void setId(Long id) {
    this.id = id;
  }

  // Getter and Setter for 'name'
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
