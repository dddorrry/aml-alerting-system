package org.rbc;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({AccountTest.class, AMLCSVReaderTest.class, AMLTransactionProcessorTest.class})

public class AMLTestSuite {
}
