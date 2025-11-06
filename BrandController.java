package phoneshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import phoneshop.phoneshop.service.BrandService; // Ensure BrandService is correctly imported
import phoneshop.phoneshop.model.Brand;  // Import the Brand entity

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

  private final BrandService service;

  // Manually add the constructor for dependency injection
  public BrandController(BrandService service) {
    this.service = service;
  }

  @GetMapping
  public List<Brand> list() { return service.list(); }

  @PostMapping
  public ResponseEntity<Brand> create(@Valid @RequestBody Brand b) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(b));
  }

  @GetMapping("/{id}")
  public Brand get(@PathVariable Long id) { return service.get(id); }

  @PutMapping("/{id}")
  public Brand update(@PathVariable Long id, @Valid @RequestBody Brand b) {
    return service.update(id, b);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) { service.delete(id); }
}
