package ai.infrrd.idc.receipt.fieldextractor.merchantname;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.MerchantNameFromTopOfTextExtractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Value;

@RunWith(JUnit4.class)
public class MerchantNameFromTopTest {


    @Test
    public void testOne(){
//        MerchantNameFromTopOfTextExtractor.initializeSpellcheck();
        MerchantNameFromTopOfTextExtractor merchantNameFromTopOfTextExtractor = new MerchantNameFromTopOfTextExtractor();

    }
}
