package ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.filterinterface;

import java.util.List;
import java.util.Map;

public interface MerchantSubgroupService {

    public List<Map<String, Object>> getSubgroups(String merchantName );

}
