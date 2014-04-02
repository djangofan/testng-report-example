package qa.tests;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import qa.test.Data;
import qa.test.TestBase;

public class Test3 extends TestBase {	

	@Test( dataProviderClass = Data.class, dataProvider = "csv" )
	public void testCsv3( String name, HashMap<String,String> rowData ) {
		rowData.put("threadId", Long.toString( threadId ) );
		rowData.put( "threadName",  threadName );
		logger.info( "Row: " + rowData );
		Assert.assertTrue( true, "Error: Test " + name );
	}

}
