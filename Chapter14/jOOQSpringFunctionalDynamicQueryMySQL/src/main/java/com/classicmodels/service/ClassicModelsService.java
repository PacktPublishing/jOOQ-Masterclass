package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.SaleRecord;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void c() {

        List<SaleRecord> r = classicModelsRepository.filterByfd(
                SALE, s -> s.SALE_.gt(4000d), s -> s.FISCAL_YEAR.eq(2004));
        System.out.println("r=" + r.get(0).getSale());       

        List<org.jooq.Record> d = classicModelsRepository
                .filterBydfds(SALE, () -> List.of(SALE.SALE_ID, SALE.FISCAL_YEAR),
                        () -> List.of(SALE.SALE_.gt(4000d), SALE.FISCAL_YEAR.eq(2004)));

        System.out.println("rzzzzz=" + d);
        
         List<org.jooq.Record> z = classicModelsRepository
                .filterBydfd(SALE, () -> List.of(SALE.SALE_ID, SALE.FISCAL_YEAR),
                        s -> s.SALE_.gt(4000d), s -> s.FISCAL_YEAR.eq(2004));

        System.out.println("rzzzzz=" + z);

        List<org.jooq.Record> zx = classicModelsRepository
                .filterBydfdddd(SALE, () -> List.of(OFFICE.OFFICE_CODE, SALE.FISCAL_YEAR),
                        s -> s.SALE_.gt(4000d), s -> s.FISCAL_YEAR.eq(2004));

        System.out.println("rzzzzz=" + zx);
        
    }
}
