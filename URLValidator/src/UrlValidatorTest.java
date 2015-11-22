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
import java.util.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
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

	private String testURL = null;
	
	/*URL Components for Partition 1*/
	private String[] schemes1 = new String[5];
	private String[] hosts1 = new String[3];
	private String[] ports1 = new String[3];
	private String[] paths1 = new String[6];
	private String[] queries1 = new String[3];
	private String[] fragments1 = new String[3];
	
	/*URL Components for Partition 2*/
	private String[] schemes2 = new String[12];
	private String[] hosts2 = new String[5];
	private String[] tlds2 = new String[15];
	private String[] ports2 = new String[11];
	private String[] paths2 = new String[9];
	private String[] queries2 = new String[11];
	private String[] fragments2 = new String[8];
	
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
		
		//set up valid hosts
		hosts2[0] = "ebay";
		hosts2[1] = "google";
		hosts2[2] = "www.google";

		//set up top-level domains
		tlds2[0] = ".arpa";
		tlds2[1] = ".ca";
		tlds2[2] = ".com";
		tlds2[3] = ".gov";
		tlds2[4] = ".int";
		tlds2[5] = ".mil";
		tlds2[6] = ".net";
		tlds2[7] = ".org";
		tlds2[8] = ".edu";
		tlds2[9] = ".co.uk";
		
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
		queries2[2] = "?action=edit+";
		queries2[3] = "?action=edit+edit2";
		
		
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
		//List<String> failedTests = new ArrayList<String>();
		System.out.println("TRACE: testManualTest()");	   
		//UrlValidator uv = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
		UrlValidator uv = new UrlValidator();
		
		//Manual tests of valid URL's
		try{
			testURL = "ftp://www.amazon.com";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
					
			testURL = "http://www.amazon.com";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		

			testURL = "http://www.google.com";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		

			testURL = "http://www.go.com/test1";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		

			testURL = "http://google.com/test1";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		

			testURL = "http://google.com/test1";
			assertEquals(testURL, true, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		

			testURL = "http://www.go.com/test1#fra";
			assertEquals(testURL, true, uv.isValid(testURL));
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));
			
			/* Identifies Bug */
			testURL = "http://www.go.com/test1?qr=ans#fra"; //FAILS, causes assertion error
			assertEquals(testURL, true, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));	
				
		} catch(AssertionError e){										
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}
		
		/* Manual tests of invalid URL's */
		try{
			testURL = "https://///www.google.co";
			assertEquals(testURL, false, uv.isValid(testURL)); //should be an invalid url due to missing required components
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));	
			
			testURL = "f://goog .c";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));	
			
			testURL = " ://google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));	
			
			testURL = " //google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = " google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "h:/google.c";
			assertEquals(testURL, false, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));	
			
			testURL = "htp:///a/b/c./def.ghi.jkl/#";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
		} catch(AssertionError e){
			System.out.println("   FAIL:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
		}
	
	}


	/** 
	 * Tests URLs with missing components
	 */
	public void testYourFirstPartition() {
		System.out.println("TRACE: testYourFirstPartition()");	   
		UrlValidator uv = new UrlValidator();

		//missing scheme
		try{
			testURL = "www.amazon.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "www.amazon.com:80";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "google.com:65535";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			
		}catch(AssertionError e){
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}
		
		//missing host
		try{
			testURL = "ftp://.ca";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://.ca";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://.ca:80";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "http://.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "http://.com/test1";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "https://.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://.co.uk";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://.co.uk/";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://.co.uk/test1";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			
		}catch(AssertionError e){
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}
		
		//missing top-level domain
		try{
			testURL = "ftp://go";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://google";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "http://google/";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "http://google.";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
			testURL = "http://www.google";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://www.google.";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
			
		}catch(AssertionError e){
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}
		
		//missing port
		try{
			testURL = "http://www.go.com/";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		} catch(AssertionError e){										
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}
	 
		
		//missing path
		try{
			testURL = "http://www.go.com";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		
		} catch(AssertionError e){										
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}
		
		//missing query
		try{
			testURL = "ftp://go.com";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		
		} catch(AssertionError e){										
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}
		
		
		//missing fragment
		try{
			testURL = "https://go.com ";
			assertEquals(testURL, true, uv.isValid(testURL)); 
			System.out.println("   PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		
		} catch(AssertionError e){										
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}

	}

	
	
	/** 
	 * Grammatical/Syntactical errors: extra slashes
	 */
	public void testYourSecondPartition() {
		System.out.println("TRACE: testYourSecondPartition()");	
		UrlValidator uv = new UrlValidator();

		try{
			testURL = "ftp:///www.google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http:///www.google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http:////www.google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://///www.google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://////www.google.com";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://www.google.com//";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://www.google.com/page//";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			testURL = "http://www.google.com/page1/page2//";
			assertEquals(testURL, false, uv.isValid(testURL));  
			System.out.println("   PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));

			
		}catch(AssertionError e){
			System.out.println("   FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
		}


	}

	/**
	 * Testing for allowed substrings in URLs
	 * Checking for errors in top-level domains, common port numbers and encoded URLs
	 * Top level domains ie: .ca .com .au .co.uk
	 * Encoded URLs ie: /%20(valid)
	 */
	public void testYourThirdPartition(){

		// TODO: ADD MORE TOP LEVEL DOMAINS
		
		// testing top level domains: all of these urls should evaluate to true
		System.out.println("TRACE: testYourThirdPartition()");	
		UrlValidator uv = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
		
		String[] myUrls = new String[1];
		myUrls[0] = "http://www.is.co.za/";
		
		for (int i = 0; i < myUrls.length; i++)
		{
			try{
				assertEquals(myUrls[i], true, uv.isValid(myUrls[i])); 
				System.out.println("   PASS:  "+myUrls[i]+"   expected=true, isValid()="+uv.isValid(myUrls[i]));		
			} catch(AssertionError e){										
				System.out.println("   FAIL:  "+myUrls[i]+"   expected=true, isValid()="+uv.isValid(myUrls[i]));		
			}
		}
		
		// testing common numbers: all of these urls should evaluate to true
		String[] myPorts = new String[17];
		myPorts[0] = "http://192.0.2.10/index.html";				// ip4v address
		myPorts[1] = "http://142.42.1.1:8080/";						// ip4v address with port
		myPorts[2] = "http://142.42.1.1:20/";
		myPorts[3] = "http://142.42.1.1:21/";
		myPorts[4] = "http://142.42.1.1:23/";
		myPorts[5] = "http://142.42.1.1:25/";
		myPorts[6] = "http://142.42.1.1:53/";
		myPorts[7] = "http://142.42.1.1:80/";
		myPorts[8] = "http://142.42.1.1:110/";
		myPorts[9] = "http://142.42.1.1:119/";
		myPorts[10] = "http://142.42.1.1:143/";
		myPorts[11] = "http://142.42.1.1:161/";
		myPorts[12] = "http://142.42.1.1:194/";
		myPorts[13] = "https://142.42.1.1:443/";					// commonly used for https
		myPorts[14] = "http://142.42.1.1:465/";
		myPorts[15] = "http://142.42.1.1:8443/";
		myPorts[16] = "http://142.42.1.1:1470/";

		for (int i = 0; i < myPorts.length; i++)
		{
			try{
				assertEquals(myPorts[i], true, uv.isValid(myPorts[i])); 
				System.out.println("   PASS:  "+myPorts[i]+"   expected=true, isValid()="+uv.isValid(myPorts[i]));		
			} catch(AssertionError e){										
				System.out.println("   FAIL:  "+myPorts[i]+"   expected=true, isValid()="+uv.isValid(myPorts[i]));		
			}
		}
		
		//URL encoding replaces unsafe ASCII characters with a "%" followed by two hexadecimal digits.
		//URLs cannot contain spaces. URL encoding normally replaces a space with a plus (+) sign or with %20.
		System.out.println("\nEncoded Strings");
		String[] encodedStrPass = new String[5];
		encodedStrPass[0] = "http://www.myURL.com/%20";
		encodedStrPass[1] = "http://www.myURL.com/abc%20def.html";
		encodedStrPass[2] = "http://www.myURL.com/abc%3E";
		encodedStrPass[3] = "http://www.myURL.com/abc%3Edef%3F";
		encodedStrPass[4] = "http://www.myURL.com/abc%3Edef%3F/%E2%80%98backtick%E2%80%98";

		for (int i = 0; i < encodedStrPass.length; i++)
		{
			try{
				assertEquals(encodedStrPass[i], true, uv.isValid(encodedStrPass[i])); 
				System.out.println("   PASS:  "+encodedStrPass[i]+"   expected=true, isValid()="+uv.isValid(encodedStrPass[i]));		
			} catch(AssertionError e){										
				System.out.println("   FAIL:  "+encodedStrPass[i]+"   expected=true, isValid()="+uv.isValid(encodedStrPass[i]));		
			}
			
		}
		
		String[] encodedStrFail = new String[5];
		encodedStrFail[0] = "%20http://www.myURL.com/";
		encodedStrFail[1] = "%20http://www.myURL.com/%20";
		encodedStrFail[2] = "http://www.%20m%20y%20U%20R%20L%20.com/";
		encodedStrFail[3] = "http://www.%20m%20y%20U%20R%20L%20.com/";
		encodedStrFail[4] = "http://www.myURL.com%18/abc%18";
		
		for (int i = 0; i < encodedStrFail.length; i++)
		{
			try{
				assertEquals(encodedStrFail[i], false, uv.isValid(encodedStrFail[i])); 
				System.out.println("   PASS:  "+encodedStrFail[i]+"   expected=false, isValid()="+uv.isValid(encodedStrFail[i]));		
			} catch(AssertionError e){										
				System.out.println("   FAIL:  "+encodedStrFail[i]+"   expected=false, isValid()="+uv.isValid(encodedStrFail[i]));		
			}
		}
	}

	/** 
	 * Test URL inputs with syntactical errors other than missing components 
	 * Grammatical/Syntactical errors: whitespace, file://, extra slashes, missing slashes, case sensitivity
	 *  Components in the wrong order???
	 */
	public void testYourFourthPartition(){
		System.out.println("TRACE: testYourFourthPartition()");	
		UrlValidator uv = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);

		/*schemes that should pass */
		String[] myUrls = new String[3];
		myUrls[0] = "http://www.google.com/";			// valid address
		myUrls[1] = "HTTP://www.google.com/";			// testing case insensitivity on scheme
		myUrls[2] = "http://www.GOOGLE.COM/";			// testing case insensitivity on host subcomponent		
		
		for (int i = 0; i < myUrls.length; i++)
		{
			try{
				assertEquals(myUrls[i], true, uv.isValid(myUrls[i])); 
				System.out.println("   PASS:  "+myUrls[i]+"   expected=true, isValid()="+uv.isValid(myUrls[i]));		
			} catch(AssertionError e){										
				System.out.println("   FAIL:  "+myUrls[i]+"   expected=true, isValid()="+uv.isValid(myUrls[i]));		
			}
		}
	}
	
	/**
	 *  Programmatic tests 
	 */
	public void testIsValid() {
		System.out.println("TRACE: testIsValid()");	   

		/* Test missing components programmatically */
		
		//add empty (missing) components
		schemes1[schemes1.length-1] = hosts1[hosts1.length-1] = ports1[ports1.length-1] = 
				paths1[paths1.length-1] = queries1[queries1.length-1] = fragments1[fragments1.length-1] = "";
				
		UrlValidator uv = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
		System.out.println(" -- Testing URLs with missing components --");
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
										assertEquals(testURL, true, uv.isValid(testURL)); 
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
		
		/* Test syntactically invalid URLs programmatically */
		System.out.println(" -- Testing syntatically incorrect URLs --");
		UrlValidator urlVal2 = new UrlValidator(schemes2); //only the schemes in schemes2 are valid schemes

		//add invalid schemes
		schemes2[4] = " h";
		schemes2[5] = "h";
		schemes2[6] = "hp://";
		schemes2[7] = "htp://";
		schemes2[8] = "htps://";
		schemes2[9] = "http:"; //missing
		schemes2[10] = "http:/";
		schemes2[11] = "http:// ";
		
		//add invalid hosts
		hosts2[2] = "abc.co m";
		hosts2[3] = " abc.com";
		hosts2[4] = "ab%20c.com";
				
		//add invalid top-level domains
		tlds2[10] = "."; 
		tlds2[11] = "..";
		tlds2[12] = ".%20";
		tlds2[13] = ".c%20om";
		tlds2[14] = ".%20com";
												
		//add invalid ports
		ports2[3] = "0.5";
		ports2[4] = "-10";
		ports2[5] = "12345";
		ports2[6] = "123456";
		ports2[7] = "1234567";
		ports2[8] = "8 0";
		ports2[9] = " 80";
		ports2[10] = "80 ";
				
		//add invalid paths, 
		paths2[5] = "//";
		paths2[6] = "///";
		paths2[7] = "/anch//";
		paths2[8] = " /";
		
		//add invalid queries
		/*queries2[4] = "?q = a";
		queries2[5] = " ?q%20=a+";
		queries2[6] = "?q1=a1+ ";
		queries2[7] = "?q1 a1%20&q2=a2";
		queries2[8] = "?q1=a1&q2=a2 ";
		queries2[9] = "?q1=a1+a2 ";
		queries2[10] = "?q1=a1&q2=a2 ";*/
		
		//add invalid fragments
		fragments2[2] = " ";
		fragments2[3] = "# ";
		fragments2[4] = " #";
		fragments2[5] = "# abc";
		fragments2[6] = "#anch ";
		fragments2[7] = "# anch ";
		
		for(int i=0, schLen=schemes2.length; i<schLen; i++){
			for(int j=0, hosLen=hosts2.length; j<hosLen; j++){
				for(int k=0, tldLen=tlds2.length; k<tldLen; k++){
					for(int l=0, porLen=ports2.length; l<porLen; l++){
						for(int m=0, patLen=paths2.length; m<patLen; m++){
							for(int o=0, refLen=fragments2.length; o<refLen; o++){
								testURL = 
										new StringBuilder().append(schemes2[i]).append(hosts2[j]).append(tlds2[k])
										.append(ports2[l]).append(paths2[m]).append(fragments2[o]).toString();								
								if(i>3 || j>1 || k>9 || l>2 || m>4 || o>1){	//starting at invalid schemes
									try{
										assertEquals(testURL, false, uv.isValid(testURL)); //should be an invalid url due to missing required components
										//System.out.println("  PASS:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));	
									} catch(AssertionError e){
										System.out.println("  FAIL:  "+testURL+"   expected=false, isValid()="+uv.isValid(testURL));
									}
								}else{
									try{
										assertEquals(testURL, true, uv.isValid(testURL)); 
										//System.out.println("  PASS:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
									} catch(AssertionError e){										
										System.out.println("  FAIL:  "+testURL+"   expected=true, isValid()="+uv.isValid(testURL));		
										
									}
								}
								
							}
						}
					}
				}
			}
		}
			
		

	}
	

	/*
	 * Further investigations on individual failed tests
	 * 
	 * */
	public void testAnyOtherUnitTest() {
		System.out.println("TRACE: testAnyOtherUnitTest()");	   
		UrlValidator uv = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);

		List<String> failedTests = new ArrayList<String>();
		failedTests.add("http://ebay.arpa");
		failedTests.add("http://ebay.arpa:80");
		failedTests.add("http://ebay.com:500");
		failedTests.add("http://ebay.com:999");
		failedTests.add("http://ebay.com:1000");
		failedTests.add("http://ebay.com:5000");
		failedTests.add("http://ebay.com:10000");
		failedTests.add("http://ebay.com:65535");
		failedTests.add("http://ebay.arpa:80/test1#");
		failedTests.add("http://ebay.arpa:80/test1#anchr");
		failedTests.add("http://ebay.arpa:80/t123#");
		failedTests.add("http://ebay.arpa:80/t123#anchr");
		failedTests.add("http://ebay.arpa:80/test1/#");
		failedTests.add("http://ebay.arpa:80/test1/#anchr");
		failedTests.add("http://ebay.arpa:80/test1/file#");
		failedTests.add("http://ebay.arpa:80/test1/file#anchr");
		failedTests.add("http://ebay.arpa:80/test1/file#");
		failedTests.add("http://ebay.arpa:80/test1/file#");

		System.out.println("-- Further Investigations:-- ");
		for(String f:failedTests){
			try{
				assertEquals(f, true, uv.isValid(f));  
				System.out.println("   PASS:  "+f+"   expected=true, isValid()="+uv.isValid(f));
			}catch(AssertionError e){
				System.out.println("   FAIL:  "+f+"   expected=true, isValid()="+uv.isValid(f));		
			}
		}
		

	}
	
	
	public void testPrintFailedTests(List<String> failedTests){
		//UrlValidatorTest vt = new UrlValidatorTest("url test");
	    System.out.println("\n***FAILED TESTS SUMMARY***");
		for(String ele : failedTests){
	        System.out.println("  "+ele);
	    }
		System.out.println("**************************");
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
		vt.printFailedTests();

	}*/
	
}
