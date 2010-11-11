/**
 * 
 */
package de.topicmapslab.format_estimator;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.topicmapslab.format_estimator.FormatEstimator.Format;

/**
 * @author uta
 * 
 */
public class FormatEstimatorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.format_estimator.FormatEstimator#guessFormat(java.io.Reader)}
	 * .
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testGuessFormatReader() throws FileNotFoundException, IOException {
		String path = "src/test/resources/";
		for (String filename : new File(path).list()) {
			Format f = FormatEstimator.guessFormat(new FileReader(path + filename));
			System.err.println(f + " | " + filename);
		}
	}

	// /**
	// * Test method for {@link
	// de.topicmapslab.format_estimator.FormatEstimator#guessFormat(java.io.Reader,
	// int)}.
	// */
	// @Test
	// public void testGuessFormatReaderInt() {
	// fail("Not yet implemented");
	// }

}
