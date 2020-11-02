package com.classicmodels;

import com.classicmodels.service.CustomerOrderManagementService;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {
 private static final String CLASS_EXTENSION = ".class";
    private static final Logger logger = Logger.getLogger(MainApplication.class.getName());

    private final CustomerOrderManagementService customerOrderManagementService;

    public MainApplication(CustomerOrderManagementService customerOrderManagementService) {
        this.customerOrderManagementService = customerOrderManagementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            
            
/*
            System.out.println("Fetching customers ordered by credit limit:");
            List<Customer> result1 = customerOrderManagementService.fetchCustomersOrderedByCreditLimit();
            System.out.println(result1);
            
            System.out.println("Fetching customers by phone:");
            List<Customer> result2 = customerOrderManagementService.fetchCustomerByPhone("03 9520 4555");
            System.out.println(result2);
            
            System.out.println("Fetching orders status:");
            List<String> result3 = customerOrderManagementService.fetchOrderStatus();
            System.out.println(result3);
            
            System.out.println("Fetching order by id:");
            Order result4 = customerOrderManagementService.fetchOrderById(10101L);
            System.out.println(result4);
            
            System.out.println("Fetching first 10 customers:");
            List<com.classicmodels.entity.Customer> result5 = customerOrderManagementService.fetchTop10By();
            System.out.println(result5);
            
            System.out.println("Fetching first 5 orders by status ordered by shipped date:");
            List<com.classicmodels.entity.Order> result6 
                    = customerOrderManagementService.fetchFirst5ByStatusOrderByShippedDateAsc("Shipped");
            System.out.println(result6);
*/
List<Class<?>> list = fetchClassesFromDirectory(
        Paths.get("target/classes/jooq/generated/tables/daos").toFile(), "jooq.generated.tables.daos");
System.out.println(Paths.get("target/classes/jooq/generated/tables/daos").toAbsolutePath());
System.out.println("list="+list);

for(Class<?> c : list) {
    printInterface(c);
}
        };        
    }
    
    public List<Class<?>> fetchClassesFromDirectory(File directory, String packageName)
            throws IOException {

        List<Class<?>> classes = new ArrayList<>();

        logger.log(Level.INFO, "Processing directory: {0}", directory);

        String[] files = directory.list();
        for (String file : files) {

            String className = null;
            if (file.endsWith(CLASS_EXTENSION)) {
                className = packageName + '.'
                        + file.substring(0, file.lastIndexOf(CLASS_EXTENSION));
            }

            if (className != null) {
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    logger.log(Level.SEVERE, "Cannot instantiate: [{0}] {1}", new Object[]{className, e});
                }
            }

            File subDir = new File(directory, file);
            if (subDir.isDirectory()) {
                classes.addAll(fetchClassesFromDirectory(subDir, packageName + '.' + file));
            }
        }

        return classes;
    }

    
    public void printInterface(Class<?> clazz) {
    System.err.println("public interface I" + clazz.getSimpleName() + "{");
    for (Method m : clazz.getDeclaredMethods()) {
        String params = "";
        for (Parameter p : m.getParameters()) {
            if (!"".equals(params))
                params += ", ";
            params += p.getType().getTypeName() + " value";
        }
        System.err.println("  " + m.getGenericReturnType().getTypeName() + " " + m.getName() + "(" + params + ");");
    }
    for (Class<?> c : clazz.getDeclaredClasses()) {
        printInterface(c);
    }
    System.err.println("}");
}
    
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}
