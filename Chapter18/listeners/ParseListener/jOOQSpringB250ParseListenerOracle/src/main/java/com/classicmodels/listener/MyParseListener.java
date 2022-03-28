package com.classicmodels.listener;

import java.util.ArrayList;
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
            pcx.parse('\'');
            char separator = pcx.character(); // get the separator of CONCAT_WS()
            pcx.position(pcx.position() + 1); // move to next position (jump over the separator)
            pcx.parse('\'');
            pcx.parse(',');

            List<Field<?>> fields = new ArrayList<>(); // extract the variadic list of fields
            fields.add(pcx.parseField());  // get the first field (assuming at least one)

            while (pcx.parseIf(",")) { // get the rest of fields (if any)
                fields.add(pcx.parseField());
            }

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
