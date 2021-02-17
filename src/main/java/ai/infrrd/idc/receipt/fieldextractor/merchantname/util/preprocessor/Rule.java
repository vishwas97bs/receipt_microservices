package ai.infrrd.idc.receipt.fieldextractor.merchantname.util.preprocessor;

import java.util.List;

public abstract class Rule {
    abstract public List<LineItemResult> apply(List<String> lines );

}
