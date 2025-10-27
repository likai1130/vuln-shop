package com.vuln.config;

import com.vuln.entity.Product;
import com.vuln.entity.User;
import com.vuln.repository.ProductRepository;
import com.vuln.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("iPhone 15 Pro", "最新款iPhone，配备A17 Pro芯片", 7999.0, "https://www.apple.com/v/iphone/home/cf/images/overview/select/iphone_17pro__0s6piftg70ym_large_2x.jpg"));
            productRepository.save(new Product("MacBook Pro", "M3芯片MacBook Pro，性能强劲", 12999.0, "https://www.apple.com/assets-www/en_WW/mac/01_product_tile/xlarge/mba_13_15_140e630d3_2x.jpg"));
            productRepository.save(new Product("AirPods Pro", "主动降噪无线耳机", 1999.0, "https://www.apple.com/v/airpods-pro/q/images/overview/noise-control/gallery/noise_control_voice_isolation__c6b9qlmdbx4y_large_2x.jpg"));
            productRepository.save(new Product("Apple Watch", "健康监测智能手表", 2999.0, "https://www.apple.com/v/watch/bt/images/overview/select/product_s11__c23ym6fc09me_large_2x.png"));
            productRepository.save(new Product("iPad Pro", "M2芯片iPad Pro，创作利器", 6799.0, "https://www.apple.com/assets-www/en_WW/ipad/01_product_tile/xlarge/ipad_pro_90a3188c9_2x.jpg"));
            productRepository.save(new Product("Magic Keyboard", "iPad专用键盘", 2399.0, "https://www.apple.com/v/ipad-keyboards/p/images/overview/selector/selector_magic_keyboard_ipad_pro__c1obcq3hlaoi_large_2x.jpg"));
        }
        
        // 初始化用户数据
        if (userRepository.count() == 0) {
            // 管理员用户
            userRepository.save(new User("admin", "admin123", "admin@vulnshop.com", "13800000000", 
                "系统管理员", "北京市朝阳区三里屯街道1号", "北京市朝阳区三里屯街道1号"));
            
            // 普通用户数据
            userRepository.save(new User("zhangsan", "123456", "zhangsan@email.com", "13800000001", 
                "张三", "上海市浦东新区陆家嘴金融贸易区世纪大道100号", "上海市浦东新区陆家嘴金融贸易区世纪大道100号"));
            
            userRepository.save(new User("lisi", "123456", "lisi@email.com", "13800000002", 
                "李四", "广州市天河区珠江新城花城大道85号", "广州市天河区珠江新城花城大道85号"));
            
            userRepository.save(new User("wangwu", "123456", "wangwu@email.com", "13800000003", 
                "王五", "深圳市南山区科技园南区深南大道10000号", "深圳市南山区科技园南区深南大道10000号"));
            
            userRepository.save(new User("zhaoliu", "123456", "zhaoliu@email.com", "13800000004", 
                "赵六", "杭州市西湖区文三路259号", "杭州市西湖区文三路259号"));
            
            userRepository.save(new User("qianqi", "123456", "qianqi@email.com", "13800000005", 
                "钱七", "南京市鼓楼区中山路321号", "南京市鼓楼区中山路321号"));
            
            userRepository.save(new User("sunba", "123456", "sunba@email.com", "13800000006", 
                "孙八", "成都市锦江区红星路三段1号", "成都市锦江区红星路三段1号"));
            
            userRepository.save(new User("zhoujiu", "123456", "zhoujiu@email.com", "13800000007", 
                "周九", "武汉市江汉区解放大道688号", "武汉市江汉区解放大道688号"));
            
            userRepository.save(new User("wushi", "123456", "wushi@email.com", "13800000008", 
                "吴十", "西安市雁塔区高新路52号", "西安市雁塔区高新路52号"));
            
            userRepository.save(new User("zhengshiyi", "123456", "zhengshiyi@email.com", "13800000009", 
                "郑十一", "重庆市渝中区解放碑步行街1号", "重庆市渝中区解放碑步行街1号"));
            
            userRepository.save(new User("wangshier", "123456", "wangshier@email.com", "13800000010", 
                "王十二", "天津市和平区南京路219号", "天津市和平区南京路219号"));
            
            userRepository.save(new User("linshisan", "123456", "linshisan@email.com", "13800000011", 
                "林十三", "青岛市市南区香港中路8号", "青岛市市南区香港中路8号"));
            
            userRepository.save(new User("chenshisi", "123456", "chenshisi@email.com", "13800000012", 
                "陈十四", "大连市中山区人民路9号", "大连市中山区人民路9号"));
            
            userRepository.save(new User("yangshiwu", "123456", "yangshiwu@email.com", "13800000013", 
                "杨十五", "厦门市思明区鹭江道8号", "厦门市思明区鹭江道8号"));
            
            userRepository.save(new User("huangshiliu", "123456", "huangshiliu@email.com", "13800000014", 
                "黄十六", "福州市鼓楼区五四路111号", "福州市鼓楼区五四路111号"));
            
            userRepository.save(new User("xushigi", "123456", "xushigi@email.com", "13800000015", 
                "徐十七", "长沙市芙蓉区五一大道389号", "长沙市芙蓉区五一大道389号"));
            
            userRepository.save(new User("zhushiba", "123456", "zhushiba@email.com", "13800000016", 
                "朱十八", "南昌市东湖区八一大道357号", "南昌市东湖区八一大道357号"));
            
            userRepository.save(new User("heishijiu", "123456", "heishijiu@email.com", "13800000017", 
                "何十九", "合肥市庐阳区长江中路369号", "合肥市庐阳区长江中路369号"));
            
            userRepository.save(new User("luershi", "123456", "luershi@email.com", "13800000018", 
                "罗二十", "郑州市金水区花园路88号", "郑州市金水区花园路88号"));
            
            userRepository.save(new User("gaoershiyi", "123456", "gaoershiyi@email.com", "13800000019", 
                "高二十一", "济南市历下区泉城路180号", "济南市历下区泉城路180号"));
            
            // 设置管理员权限
            User adminUser = userRepository.findByUsername("admin").orElse(null);
            if (adminUser != null) {
                adminUser.setIsAdmin(true);
                userRepository.save(adminUser);
            }
        }
    }
}
