package qa.test1;

import org.apache.metamodel.query.SelectItem;
import org.testng.annotations.Test;

import qa.test.Data;

import com.google.common.base.Joiner;

public class Test2 extends Data {	

	@Test( dataProvider = "csv" )
	public void testCsv( SelectItem[] cols, Object[] data ) {
		data[3] = Long.toString( Thread.currentThread().getId() );
		data[4] = Thread.currentThread().getName();
		String aRow = Joiner.on("|").join( data );
		logger.info( aRow );  
	}

}
