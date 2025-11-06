package phoneshop.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phoneshop.phoneshop.model.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> { }
