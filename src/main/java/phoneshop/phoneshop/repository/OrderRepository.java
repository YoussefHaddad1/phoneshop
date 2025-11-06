package phoneshop.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phoneshop.phoneshop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> { }
