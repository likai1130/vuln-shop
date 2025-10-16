package com.vuln.config;

import com.vuln.entity.Product;
import com.vuln.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("iPhone 15 Pro", "最新款iPhone，配备A17 Pro芯片", 7999.0, "https://via.placeholder.com/300x200?text=iPhone+15+Pro"));
            productRepository.save(new Product("MacBook Pro", "M3芯片MacBook Pro，性能强劲", 12999.0, "https://via.placeholder.com/300x200?text=MacBook+Pro"));
            productRepository.save(new Product("AirPods Pro", "主动降噪无线耳机", 1999.0, "https://via.placeholder.com/300x200?text=AirPods+Pro"));
            productRepository.save(new Product("Apple Watch", "健康监测智能手表", 2999.0, "https://via.placeholder.com/300x200?text=Apple+Watch"));
            productRepository.save(new Product("iPad Pro", "M2芯片iPad Pro，创作利器", 6799.0, "https://via.placeholder.com/300x200?text=iPad+Pro"));
            productRepository.save(new Product("Magic Keyboard", "iPad专用键盘", 2399.0, "https://via.placeholder.com/300x200?text=Magic+Keyboard"));
        }
    }
}
