package phoneshop.phoneshop.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import phoneshop.phoneshop.exception.NotFoundException;
import phoneshop.phoneshop.model.Phone;
import phoneshop.phoneshop.repository.PhoneRepository;
import phoneshop.phoneshop.dto.PhoneDTO;

import java.util.List;

@Service
public class PhoneService {

  private final PhoneRepository phoneRepo;

  public PhoneService(PhoneRepository phoneRepo) {
    this.phoneRepo = phoneRepo;
  }

  public Phone create(Phone p) {
    return phoneRepo.save(p);
  }

  @Transactional
  public PhoneDTO update(Long id, Phone data) {
    Phone p = phoneRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Phone not found: " + id));

    p.setModel(data.getModel());
    p.setPrice(data.getPrice());
    p.setStock(data.getStock());
    p.setSpecs(data.getSpecs());
    if (data.getBrand() != null) {
      p.setBrand(data.getBrand());
    }
    phoneRepo.save(p);

    // Reload with brand fetched
    Phone reloaded = phoneRepo.findByIdWithBrand(p.getId())
            .orElseThrow(() -> new NotFoundException("Phone not found: " + id));
    return PhoneDTO.from(reloaded);
  }

  @Transactional
  public List<PhoneDTO> list() {
    return phoneRepo.findAllWithBrand().stream().map(PhoneDTO::from).toList();
  }

  @Transactional
  public PhoneDTO get(Long id) {
    return phoneRepo.findByIdWithBrand(id)
            .map(PhoneDTO::from)
            .orElseThrow(() -> new NotFoundException("Phone not found: " + id));
  }

  public void delete(Long id) {
    phoneRepo.deleteById(id);
  }

  @Transactional
  public List<Phone> searchByModel(String q) {
    return phoneRepo.findByModelContainingIgnoreCase(q);
  }

  @Transactional
  public List<Phone> searchByBrand(String brand) {
    return phoneRepo.findByBrand_NameIgnoreCase(brand);
  }
}