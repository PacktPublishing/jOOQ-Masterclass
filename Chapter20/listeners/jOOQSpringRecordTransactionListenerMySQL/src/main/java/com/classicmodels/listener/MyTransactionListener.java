package com.classicmodels.listener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import jooq.generated.tables.records.EmployeeRecord;
import org.jooq.TransactionContext;
import org.jooq.impl.DefaultTransactionListener;

public class MyTransactionListener extends DefaultTransactionListener {

    @Override
    public void commitEnd(TransactionContext ctx) {

        EmployeeRecord employee = (EmployeeRecord) ctx.configuration().data("employee");

        if (employee != null) {

            Path path = Paths.get(String.join(File.separator, System.getProperty("user.dir"),
                    "employees", employee.getExtension() + ".txt"));

            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }

                employee.formatInsert(Files.newBufferedWriter(path,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.APPEND, StandardOpenOption.WRITE));
            } catch (IOException ex) {                
                // hamdle exception                
            }
        }
        
        super.commitEnd(ctx);
    }
}
