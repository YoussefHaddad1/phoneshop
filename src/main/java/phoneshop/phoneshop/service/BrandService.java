package phoneshop.phoneshop.service;

import org.springframework.stereotype.Service;
import phoneshop.phoneshop.exception.NotFoundException;
import phoneshop.phoneshop.model.Brand;
import phoneshop.phoneshop.repository.BrandRepository;

import java.util.List;

@Service
public class BrandService {

  private final BrandRepository repo;

  // Constructor for dependency injection
  public BrandService(BrandRepository repo) {
    this.repo = repo;
  }

  public Brand create(Brand b) {
    return repo.save(b);
  }

  public List<Brand> list() {
    return repo.findAll();
  }

  public Brand get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Brand not found: " + id));
  }

  public Brand update(Long id, Brand data) {
    Brand b = get(id);
    b.setName(data.getName());  // Calling the manually written getter
    return repo.save(b);
  }

  public void delete(Long id) {
    repo.delete(get(id));
  }
}
