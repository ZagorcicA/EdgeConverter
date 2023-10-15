package utilities.parse;

import dto.Table;

import java.io.File;

public class XmlFileParser extends FileParser {


    public XmlFileParser(File constrFile) {
        super(constrFile);
        this.openFile(fileParsed);
    }

    @Override
    public void parse() throws Exception {
    }

    @Override
    public void openFile(File inputFile) {
        try {
            Table[] testTable = new Table[3];
            testTable[0] = new Table("1|This is");
            testTable[1] = new Table("2|XML File");
            testTable[2] = new Table("13|Opened");
            tables = testTable;
            this.parse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean isTableDup(String testTableName) {
        for (Object alTable : alTables) {
            Table tempTable = (Table) alTable;
            if (tempTable.getName().equals(testTableName)) {
                return true;
            }
        }
        return false;
    }
}
