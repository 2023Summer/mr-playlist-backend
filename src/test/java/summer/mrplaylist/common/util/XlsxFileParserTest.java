package summer.mrplaylist.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class XlsxFileParserTest {
    @Autowired
    private XlsxFileParser xlsxFileParser;


    @Test
    void fileRead() throws Exception {
        xlsxFileParser.fileRead();
    }
}