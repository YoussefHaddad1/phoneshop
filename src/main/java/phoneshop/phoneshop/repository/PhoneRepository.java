package phoneshop.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import phoneshop.phoneshop.model.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

  List<Phone> findByModelContainingIgnoreCase(String q);
  List<Phone> findByBrand_NameIgnoreCase(String brandName);

  // Fix LazyInitializationException — fetch the brand together
  @Query("SELECT p FROM Phone p JOIN FETCH p.brand")
  List<Phone> findAllWithBrand();

  @Query("SELECT p FROM Phone p JOIN FETCH p.brand WHERE p.id = :id")
  Optional<Phone> findByIdWithBrand(@Param("id") Long id);
}