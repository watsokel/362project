/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import junit.framework.TestCase;

/**
 * Performs Validation Test for url validations.
 *
 * @version $Revision: 1128446 $ $Date: 2011-05-27 13:29:27 -0700 (Fri, 27 May
 *          2011) $
 */
public class UrlValidatorTest extends TestCase {

	private boolean printStatus = false;
	private boolean printIndex = false;// print index that indicates current
										// scheme,host,port,path, query test
										// were using.

	
	/*URL Components for Partition 1*/
	private String[] schemes1 = new String[5];
	private String[] hosts1 = new String[3];
	private String[] ports1 = new String[3];
	private String[] paths1 = new String[6];
	private String[] queries1 = new String[3];
	private String[] fragments1 = new String[3];
	
	/*URL Components for Partition 2*/
	private String[] schemes2 = new String[10];
	private String[] hosts2 = new String[10];
	private String[] tld2 = new String[10];
	private String[] ports2 = new String[10];
	private String[] paths2 = new String[10];
	private String[] queries2 = new String[10];
	private String[] fragments2 = new String[10];
	
	/**
	 * Constructor
	 */
	public UrlValidatorTest(String testName) {
		super(testName);
	}
	
	/**
	 * Initializes arrays with valid components
	 */	
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("\nTRACE: setUp()");
		
		//set up VALID schemes
		//Source: 	HTTP:
		//			FTP: https://www.cs.tut.fi/~jkorpela/ftpurl.html
		schemes1[0] = schemes2[0] = "http://";
		schemes1[1] = schemes2[1] = "h3t://";
		schemes1[2] = schemes2[2] = "https://";
		schemes1[3] = schemes2[3] = "ftp://"; //ftp://[user:password@]host[:port][/path]
		
		//set up valid hosts
		hosts1[0] = hosts2[0] = "google.com";
		hosts1[1] = hosts2[1] = "www.google.com";
		
		//set up valid ports
		ports1[0] = ports2[0] = ":80";
		ports1[1] = ports2[1] = ":22";
		ports1[2] = ports2[2] = ":65535";
		
		//set up valid paths
		paths1[0] = paths2[0] = "/test1";
		paths1[1] = paths2[1] = "/t123";
		paths1[2] = paths2[2] = "/$23";
		paths1[3] = paths2[3] = "/test1/";
		paths1[4] = paths2[4] = "/test1/file";
		
		//set up valid queries
		queries1[0] = queries2[0] = "?action=view";
		queries1[1] = queries2[1] = "?action=edit&mode=up";
		
		//set up valid fragments
		fragments1[0] = fragments2[0] = "#";
		fragments1[1] = fragments2[1] = "#anchr";
	}
	
	/**
	 * Clean up
	 */
	protected void tearDown() throws Exception {
		super.tearDown(); //  
		System.out.println("TRACE: tearDown()\n");	  
		//TODO: cleanup code goes here 
	 }
	 
	/**
	 * Manual Tests
	 */
	public void testManualTest() {
		System.out.println("TRACE: testManualTest()");	   
		UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
		
		//Manual tests of valid URL's
		System.out.println("http://www.amazon.com  expected=true, isValid()="+urlVal.isValid("http://www.amazon.com")); //returns true
		System.out.println("http://www.google.com  expected=true, isValid()="+urlVal.isValid("http://www.google.com")); //returns true
		System.out.println("http://www.go.com/test1  expected=true, isValid()="+urlVal.isValid("http://www.go.com/test1")); //returns true
		System.out.println("http://google.com/test1  expected=true, isValid()="+urlVal.isValid("http://google.com/test1")); //returns true
		System.out.println("http://www.go.com/test1?qry=ans  expected=true, isValid()="+urlVal.isValid("http://www.go.com/test1?qry=ans")); //returns false
		System.out.println("http://www.go.com/test1?qr=ans#fra expected=true, isValid()="+urlVal.isValid("http://www.go.com/test1?qr=ans#fra")); //returns false
	
		//TODO: More manual tests!
	
	}

	/**
	 *  Test URL inputs with missing components 
	 */
	public void testYourFirstPartition() {
		System.out.println("TRACE: testYourFirstPartition()");	   

		//add empty (missing) components
		schemes1[schemes1.length-1] = hosts1[hosts1.length-1] = ports1[ports1.length-1] = 
				paths1[paths1.length-1] = queries1[queries1.length-1] = fragments1[fragments1.length-1] = "";
				
		UrlValidator uv = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
		System.out.println("\n  ASSERTION  DETAILS");

		for(int i=0, schLen=schemes1.length; i<schLen; i++){
			for(int j=0, hosLen=hosts1.length; j<hosLen; j++){
				for(int k=0, porLen=ports1.length; k<porLen; k++){
					for(int l=0, patLen=paths1.length; l<patLen; l++){
						for(int m=0, queLen=queries1.length; m<queLen; m++){
							for(int n=0, refLen=fragments1.length; n<refLen; n++){
								String testURL = 
										new StringBuilder().append(schemes1[i]).append(hosts1[j])
										.append(ports1[k]).append(paths1[l]).append(queries1[m])
										.append(fragments1[n]).toString();								
								if(schemes1[i].equals("") || hosts1[j].equals("")){	//missing scheme and/or host (required)
									//Source: http://stackoverflow.com/questions/4115602/assert-statement-causing-junit-tests-to-stop	
									try{
										assertEquals(testURL, false, uv.isValid(testURL)); //should be an invalid url due to missing required components
										//System.out.println("  PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));	
									} catch(AssertionError e){
										System.out.println("  FAIL:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
									}
								}else{
									try{
										assertEquals(testURL, true, uv.isValid(testURL)); //should be valid (no missing required components)
										//System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
									} catch(AssertionError e){										
										System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
									}
								}
								
							}
						}
					} 
				}
			}
		}		
	}
	
	/** 
	 * Test URL inputs with syntactical errors other than missing components 
	 * Grammatical/Syntactical errors: whitespace, file://, extra slashes, missing slash, encoded URLs should be valid: e.g. /%20
	 *  
	 */
	public void testYourSecondPartition() {
		System.out.println("TRACE: testYourSecondPartition()");	   
		UrlValidator urlVal = new UrlValidator(schemes2); //only the schemes in schemes2 are valid schemes

		//TODO: add invalid schemes
		//schemes2[4] = "htp://";
		//schemes2[5] = "http:"; //missing
		//
			// ... MORE ...
		
		//TODO: add invalid hosts
		//hosts2[2]
			// ... MORE ...
		
		//TODO: add invalid ports
		//ports2[3]
			// ... MORE ...
		
		//TODO: add invalid paths, queries, fragments etc

	}

	
	
	/** 
	 * Tests valid inputs ??? 3rd partition?  Components in the wrong order???
	 */
	public void testIsValid() {
		System.out.println("TRACE: testIsValid()");	   

		//for (int i = 0; i < 10000; i++) {
		//
		//}
		 
		
	}

	/*
	 * Errors in top-level domains .ca .com .au  .co.uk 
	 * Multiple queries
	 * Encoded URLs should be valid: /%20
	 * 
	 * */
	public void testAnyOtherUnitTest() {
		System.out.println("TRACE: testAnyOtherUnitTest()");	   

	}

	/**
	 * Create set of tests by taking the testUrlXXX arrays and running through
	 * all possible permutations of their combinations.
	 *
	 * @param testObjects
	 *            Used to create a url.
	 * @throws Exception 
	 */

	/* 	Main method not required: http://sqa.fyicenter.com/FAQ/JUnit/Do_You_Need_to_Write_a_main_Method_in_a_JUnit_.html
		Main method recommended: http://www.appperfect.com/support/java-coding-rules/junit.html*/
	/*public static void main(String[] argv) throws Exception {
		UrlValidatorTest vt = new UrlValidatorTest("url test");
		vt.setUp();
		vt.testIsValid();
		vt.tearDown();
	}*/
}
