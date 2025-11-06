package phoneshop.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phoneshop.phoneshop.model.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
  Optional<Brand> findByNameIgnoreCase(String name);
}
