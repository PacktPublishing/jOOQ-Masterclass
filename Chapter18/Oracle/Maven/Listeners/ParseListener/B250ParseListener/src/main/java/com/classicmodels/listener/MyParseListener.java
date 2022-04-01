package com.classicmodels.listener;

import java.util.List;
import org.jooq.Field;
import org.jooq.ParseContext;
import static org.jooq.SQLDialect.ORACLE;
import org.jooq.impl.CustomField;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.inline;
import org.jooq.impl.DefaultParseListener;
import org.jooq.impl.SQLDataType;

public class MyParseListener extends DefaultParseListener {

    @Override
    public Field parseField(ParseContext pcx) {

        if (pcx.parseFunctionNameIf("CONCAT_WS")) {
            
            pcx.parse('(');
            
            String separator = pcx.parseStringLiteral();           
            
            pcx.parse(',');

            List<Field<?>> fields = pcx.parseList(",", c -> c.parseField()); // extract the variadic list of fields

            pcx.parse(')'); // the function CONCAT_WS() was parsed                

            // prepare the Oracle emulation
            return CustomField.of("", SQLDataType.VARCHAR, f -> {
                switch (f.family()) {
                    case ORACLE -> {
                        Field result = inline("");
                        for (Field<?> field : fields) {
                            result = result.concat(DSL.nvl2(field,
                                    inline(separator).concat(field.coerce(String.class)), field));
                        }

                        f.visit(result); // visit this QueryPart
                    }

                    // case other dialect ...    
                    }
            });
        }

        // pass control to jOOQ
        return null;
    }
}
