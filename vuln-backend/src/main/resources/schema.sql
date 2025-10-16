-- 创建products表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2),
    image VARCHAR(500)
);

-- 插入测试数据
INSERT INTO products (name, description, price, image) VALUES
('iPhone 15 Pro', '最新款iPhone，配备A17 Pro芯片', 7999.00, 'https://via.placeholder.com/300x200?text=iPhone+15+Pro'),
('MacBook Pro', 'M3芯片MacBook Pro，性能强劲', 12999.00, 'https://via.placeholder.com/300x200?text=MacBook+Pro'),
('AirPods Pro', '主动降噪无线耳机', 1999.00, 'https://via.placeholder.com/300x200?text=AirPods+Pro'),
('Apple Watch', '健康监测智能手表', 2999.00, 'https://via.placeholder.com/300x200?text=Apple+Watch'),
('iPad Pro', 'M2芯片iPad Pro，创作利器', 6799.00, 'https://via.placeholder.com/300x200?text=iPad+Pro'),
('Magic Keyboard', 'iPad专用键盘', 2399.00, 'https://via.placeholder.com/300x200?text=Magic+Keyboard');
