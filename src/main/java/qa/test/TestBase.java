package qa.test;

import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.annotations.BeforeTest;

public class TestBase implements ITest {
	
	protected org.slf4j.Logger logger = LoggerFactory.getLogger( this.getClass().getSimpleName() );
	protected String threadName = Thread.currentThread().getName();
	protected long threadId = Thread.currentThread().getId();
	protected String testName;
	
	@BeforeTest
	public void setUp() {
		this.testName = getTestName();		
	}

	@Override
	public String getTestName() {
		String name = this.getClass().getSimpleName() + "-Thread" + threadId;
		return name;
	}

}
