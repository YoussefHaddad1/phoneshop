package phoneshop.phoneshop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HomeController {
    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
            "app", "phoneshop",
            "status", "OK",
            "hint", "Try /api/brands, /api/phones, /api/customers, /api/orders"
        );
    }
}
