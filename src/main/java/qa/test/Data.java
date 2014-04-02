package qa.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.DataContextFactory;
import org.apache.metamodel.csv.CsvConfiguration;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

public class Data {

	public static File csvFile = new File("src/test/resources/Test.csv");
	public static org.slf4j.Logger logger = LoggerFactory.getLogger( "DataProvider" );
	
	/**
	 * Gets a 2D Object array from a List of Row objects that is only 2 args wide.
	 * @param rows
	 * @return
	 */
	public static Object[][] get2ArgArrayFromRows( List<Row> rows ) {
		Object[][] myArray = new Object[rows.size()][2];
		int i = 0;
		SelectItem[] cols = rows.get(0).getSelectItems();
		for ( Row r : rows ) {
			String tName = "None";
			Map<String, String> tRow = new HashMap<String,String>();
			Object[] data = r.getValues();
			for ( int j = 0; j < cols.length; j++ ) {
				if ( data[j] == null ) data[j] = ""; // force empty string where there are NULL values
			}
			for ( int k = 0; k < cols.length; k++ ) {
				tRow.put( cols[k].toString(), (String)data[k] );
				if ( k == 1 ) tName = (String)data[k];
			}
			myArray[i][0] = tName;
			myArray[i][1] = tRow;
			i++;
		}
		return myArray;
	}

	public static Object[][] getCsvData( File csvFile ) 
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

	@DataProvider( name = "csv", parallel = false ) 
	public static Object[][] gatherCsvData() 
	{
		return getCsvData( csvFile );
	}

}
