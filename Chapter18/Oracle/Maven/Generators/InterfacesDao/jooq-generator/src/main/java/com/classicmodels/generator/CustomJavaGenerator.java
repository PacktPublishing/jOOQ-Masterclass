package com.classicmodels.generator;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import org.jooq.Constants;
import org.jooq.codegen.GeneratorStrategy.Mode;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UniqueKeyDefinition;
import org.jooq.tools.JooqLogger;

public class CustomJavaGenerator extends JavaGenerator {

    private static final JooqLogger log = JooqLogger.getLogger(CustomJavaGenerator.class);

    @Override
    protected void generateDao(TableDefinition table) {

        // generate DAO interface for 'table'
        File daoInterfaceFile = Paths.get(getFile(table, Mode.DAO).getAbsolutePath()
                .replace("Impl.java", ".java")).toFile();
        JavaWriter outDaoInterface = newJavaWriter(daoInterfaceFile);
        log.info("Generating DAO interface", outDaoInterface.file().getName());
        generateDaoInterface(table, outDaoInterface);
        closeJavaWriter(outDaoInterface);

        // generate DAO class for 'table'
        JavaWriter outDaoClass = newJavaWriter(getFile(table, Mode.DAO));
        log.info("Generating DAO", outDaoClass.file().getName());
        super.generateDao(table, outDaoClass);
        closeJavaWriter(outDaoClass);
    }

    protected void generateDaoInterface(TableDefinition table, JavaWriter out) {

        /* generating interfaces */
        UniqueKeyDefinition key = table.getPrimaryKey();
        if (key == null) {
            log.info("Skipping DAO interface generation", out.file().getName());
            return;
        }

        super.printPackage(out, table, Mode.DAO);
        
        final String className = out.file().getName();
        final String pType = out.ref(getStrategy().getFullJavaClassName(table, Mode.POJO));
        String tType = "Void";
        
        List<ColumnDefinition> keyColumns = key.getKeyColumns();
        if (keyColumns.size() == 1) {
            tType = getJavaType(keyColumns.get(0).getType(resolver(out, Mode.POJO)), out, Mode.POJO);
        } else if (keyColumns.size() <= Constants.MAX_ROW_DEGREE) {
            String generics = "";
            String separator = "";

            for (ColumnDefinition column : keyColumns) {
                generics += separator + out.ref(getJavaType(
                        column.getType(resolver(out, Mode.POJO)), out, Mode.POJO));
                separator = ", ";
            }
            tType = org.jooq.Record.class.getName() + keyColumns.size() + "<" + generics + ">";
        } else {
            tType = org.jooq.Record.class.getName();
        }

        tType = out.ref(tType);

        final String tranRO 
                = out.ref("org.springframework.transaction.annotation.Transactional(readOnly=true)");
        final String tranRW 
                = out.ref("org.springframework.transaction.annotation.Transactional");
        
        out.println("@%s", tranRO);
        out.println("public interface %s {", className.substring(0, className.indexOf(".java")));

        out.println("public %s getId(%s object);", tType, pType);

        for (ColumnDefinition column : table.getColumns()) {
            final String colClass = getStrategy().getJavaClassName(column);
            final String colTypeFull = getJavaType(column.getType(resolver(out, Mode.DAO)), out, Mode.DAO);
            final String colType = out.ref(colTypeFull);

            out.println("public %s<%s> fetchRangeOf%s(%s lowerInclusive, %s upperInclusive);",
                    List.class, pType, colClass, colType, colType);
       
            out.println("public %s<%s> fetchBy%s(%s... values);", List.class, pType, colClass, colType);

            for (UniqueKeyDefinition uk : column.getUniqueKeys()) {
                if (uk.getKeyColumns().size() == 1 && uk.getKeyColumns().get(0).equals(column)) {
                    out.println("public %s fetchOneBy%s(%s value);", pType, colClass, colType);
                    break;
                }
            }
        }

        // add some methods from org.jooq.DAO
        out.println("@%s", tranRW);
        out.println("public void insert(%s p);", pType);
        out.println("@%s", tranRW);
        out.println("public void insert(%s[] p);", pType);
        out.println("@%s", tranRW);
        out.println("public void insert(%s<%s> p);", out.ref("java.util.Collection"), pType);
        out.println("@%s", tranRW);
        out.println("public void update(%s p);", pType);
        out.println("@%s", tranRW);
        out.println("public void update(%s[] p);", pType);
        out.println("@%s", tranRW);
        out.println("public void update(%s<%s> p);", out.ref("java.util.Collection"), pType);
        out.println("@%s", tranRW);
        out.println("public void delete(%s p);", pType);
        out.println("@%s", tranRW);
        out.println("public void delete(%s[] p);", pType);
        out.println("@%s", tranRW);
        out.println("public void delete(%s<%s> p);", out.ref("java.util.Collection"), pType);

        out.println("}");

    }
}