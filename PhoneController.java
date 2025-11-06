package phoneshop.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import phoneshop.phoneshop.dto.PhoneDTO;
import phoneshop.phoneshop.model.Phone;
import phoneshop.phoneshop.service.PhoneService;

import java.util.List;

@RestController
@RequestMapping("/api/phones")
public class PhoneController {

  private final PhoneService service;

  @Autowired
  public PhoneController(PhoneService service) {
    this.service = service;
  }

  @GetMapping
  public List<PhoneDTO> list() {
    return service.list();
  }

  @GetMapping("/{id}")
  public PhoneDTO get(@PathVariable Long id) {
    return service.get(id);
  }

  @PostMapping
  public ResponseEntity<Phone> create(@Valid @RequestBody Phone phone) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(phone));
  }

  @PutMapping("/{id}")
  public PhoneDTO update(@PathVariable Long id, @Valid @RequestBody Phone phone) {
    return service.update(id, phone);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  // Optional search endpoints
  @GetMapping(params = "model")
  public List<Phone> searchByModel(@RequestParam String model) {
    return service.searchByModel(model);
  }

  @GetMapping(params = "brand")
  public List<Phone> searchByBrand(@RequestParam String brand) {
    return service.searchByBrand(brand);
  }
}