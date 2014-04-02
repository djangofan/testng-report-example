package qa.test;

import java.io.File;
import java.util.List;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.DataContextFactory;
import org.apache.metamodel.csv.CsvConfiguration;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.Query;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;
import org.apache.metamodel.xml.XmlSaxDataContext;
import org.apache.metamodel.xml.XmlSaxTableDef;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import com.google.common.base.Joiner;

public class Data {

	public File csvFile = new File("src/test/resources/Test.csv");
	public org.slf4j.Logger logger = LoggerFactory.getLogger( this.getClass().getSimpleName() );
	
	/**
	 * Gets a 2D Object array from a List of Row objects that is only 2 args wide.
	 * @param rows
	 * @return
	 */
	public Object[][] get2ArgArrayFromRows( List<Row> rows ) {
		Object[][] myArray = new Object[rows.size()][2];
		int i = 0;
		SelectItem[] cols = rows.get(0).getSelectItems();
		for ( Row r : rows ) {
			Object[] data = r.getValues();
			for ( int j = 0; j < cols.length; j++ ) {
				if ( data[j] == null ) data[j] = ""; // force empty string where there are NULL values
			}
			myArray[i][0] = cols;
			myArray[i][1] = data;
			i++;
		}
		//logger.info( "Row count: " + rows.size() );
		//logger.info( "Column names: " + Arrays.toString( cols ) );
		return myArray;
	}

	public Object[][] getCsvData( File csvFile ) 
	{	
		CsvConfiguration conf = new CsvConfiguration( 1 );
		DataContext csvContext = DataContextFactory.createCsvDataContext( csvFile, conf );
		Schema schema = csvContext.getDefaultSchema();
		Table[] tables = schema.getTables();
		Table table = tables[0]; // a representation of the csv file name including extension
		DataSet dataSet = csvContext.query()
				.from( table )
				.selectAll()
				.where("run").eq("Y")
				.execute();
		List<Row> rows = dataSet.toRows();
		Object[][] myArray = get2ArgArrayFromRows( rows );
		return myArray;
	}
	
	/**
	 * Example of simply printing contents of a table.
	 * @param xmlFile
	 * @param def
	 */
	public void printTableAsDefined( File xmlFile, XmlSaxTableDef def ) {
		logger.info("Printing table contents as defined: ");
		DataContext dc = new XmlSaxDataContext( xmlFile, def );
		Table employeeTable = dc.getTableByQualifiedLabel("/employee");
        Query q = dc.query().from( employeeTable )
                .selectAll()
                .toQuery();
        DataSet ds = dc.executeQuery(q);
        List<Row> rows = ds.toRows();
        for ( Row r : rows ) {
            String aRow = Joiner.on("|").join( r.getValues() );
            logger.info( aRow );
        }
	}

	@DataProvider( name = "csv", parallel = true ) 
	public Object[][] gatherCsvData() 
	{
		return getCsvData( csvFile );
	}

}
