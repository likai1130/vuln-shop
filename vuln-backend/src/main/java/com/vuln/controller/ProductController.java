package com.vuln.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuln.entity.Product;
import com.vuln.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 获取所有商品
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }
    
    /**
     * 搜索商品 - 关键漏洞点：直接返回用户输入，配合前端原型污染
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam String q) {
        try {
            // 漏洞：直接解析用户输入为JSON并返回，不进行任何过滤
            // 使用JsonNode来保持原始属性名，避免__proto__被转换
            com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(q);
            Map<String, Object> userInput = objectMapper.convertValue(jsonNode, Map.class);
            
            // 如果包含搜索关键词，执行商品搜索
            if (userInput.containsKey("keyword")) {
                String keyword = (String) userInput.get("keyword");
                List<Product> products = productRepository.findByKeyword(keyword);
                userInput.put("products", products);
            }
            
            // 关键漏洞：检查并处理原型污染攻击
            if (jsonNode.has("__proto__")) {
                com.fasterxml.jackson.databind.JsonNode protoNode = jsonNode.get("__proto__");
                Map<String, Object> protoPayload = objectMapper.convertValue(protoNode, Map.class);
                userInput.put("__proto__", protoPayload);
            }
            
            if (userInput.containsKey("testcmd")) {
                String cmd = String.valueOf(userInput.get("testcmd"));
                Process process = Runtime.getRuntime().exec(cmd);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                reader.close();
                userInput.put("cmd_output", output.toString());
            }

            return ResponseEntity.ok(userInput);
            
        } catch (Exception e) {
            // 如果JSON解析失败，检查是否是XSS攻击载荷
            if (q.contains("<script>") || q.contains("alert(") || q.contains("proto__proto__")) {
                Map<String, Object> xssResponse = new HashMap<>();
                xssResponse.put("xss_payload", q);
                xssResponse.put("message", "XSS攻击载荷检测到");
                xssResponse.put("vulnerability", "XSS + Prototype Pollution");
                return ResponseEntity.ok(xssResponse);
            }
            
            // 返回错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid JSON format");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 根据ID获取商品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }
}
