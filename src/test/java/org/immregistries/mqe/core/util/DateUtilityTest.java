package org.immregistries.mqe.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.joda.time.DateTime;
import org.junit.Test;

public class DateUtilityTest {
	private DateUtility dateUtility = DateUtility.INSTANCE;

	@Test
	public void isDateOneBeforeTwoIgnoringTimeAndZone() {
		String dateSubmitted = "20200108000000-0400";
		String adminDate = "20200108";
		boolean adminAfterSubmission = dateUtility.isDateOneBeforeTwoIgnoringTimeAndZone(dateSubmitted, adminDate);
		assertFalse("These dates should be considered the same when ignoring time and zone", adminAfterSubmission);

		String weirdDate = "20200008000000";
		String regularDate = "20200108";
		boolean isWierdDateBeforeRegularDate = dateUtility.isDateOneBeforeTwoIgnoringTimeAndZone(weirdDate, regularDate);
		assertFalse("If one of these dates isn't valid, it should return false", isWierdDateBeforeRegularDate);

		String nullDate = "20200008000000";
		regularDate = "20200108";
		boolean isNullDate = dateUtility.isDateOneBeforeTwoIgnoringTimeAndZone(nullDate, regularDate);
		assertFalse("If one of these dates is null it should return false", isNullDate);
		isNullDate = dateUtility.isDateOneBeforeTwoIgnoringTimeAndZone(regularDate, nullDate);
		assertFalse("If one of these dates is null it should return false", isNullDate);

		String dateEarlier = "20200107000000-0400";
		regularDate = "20200108";
		boolean earlierDateIsConsideredEarlier = dateUtility.isDateOneBeforeTwoIgnoringTimeAndZone(dateEarlier, regularDate);
		assertTrue("This should come back true since the date itself is earlier", earlierDateIsConsideredEarlier);

		String dateTimeLater = "20200107100000-0100";
		String timeZoneDate = "20200107220000-0400";
		earlierDateIsConsideredEarlier = dateUtility.isDateOneBeforeTwoIgnoringTimeAndZone(dateTimeLater, timeZoneDate);
		assertFalse("These should come back as the same days", earlierDateIsConsideredEarlier);

	}

	@Test
	public void testGetAgeFromDates() {
		Date today = new Date();
		today.setYear(2018);
		today.setMinutes(1);
		Date yesterYear = new Date();
		yesterYear.setYear(2000);
		yesterYear.setMinutes(0);
		int age = dateUtility.getYearsBetween(yesterYear, today);

		assertEquals("should be 18 years between", 18, age);
	}
	
	@Test
	public void testStringParsing() {
		String notADate = "saflj";
		
		assertFalse("it's garbage", dateUtility.isDate(notADate));
		
		String isADate = "20160101";
		assertTrue("it's good, really[" + isADate + "]", dateUtility.isDate(isADate));
//		System.out.println(dateUtility.parseDate(isADate));
		isADate = "2016-01-01";
		assertFalse("it's bad, really[" + isADate + "]", dateUtility.isDate(isADate));
//		System.out.println(dateUtility.parseDate(isADate));
		
		isADate = "19000101";
		assertTrue("it's good, really[" + isADate + "]", dateUtility.isDate(isADate));
//		System.out.println(dateUtility.parseDate(isADate));
		
	}
	@Test
	public void emptyDateParsing() {
		String notADate = "";
		
		assertFalse("it's emptyString", dateUtility.isDate(notADate));
		
		String nullDate = null;
		
		assertFalse("it's null, really[" + nullDate + "]", dateUtility.isDate(nullDate));
		 
	}
	
	@Test
	public void beforeOrAfterTesting1() {
		String date = "20160101";
		String notBefore = "20000101";
		String notAfter  = "20161231";
		
		boolean beforeOrAfter = dateUtility.isOutsideOfRange(date, notBefore, notAfter);
		assertFalse("Date " + date + " is within dates notBefore[" + notBefore + "] notAfter[" + notAfter + "]", 
				beforeOrAfter);
		
		date = "20200101";
		beforeOrAfter = dateUtility.isOutsideOfRange(date, notBefore, notAfter);
		assertTrue("Date " + date + " is NOT within dates notBefore[" + notBefore + "] notAfter[" + notAfter + "]", 
				beforeOrAfter);
	}
	
	@Test
	public void beforeOrAfterTesting2() {
		String date = "20160101";
		String notBefore = null;
		String notAfter  = "20121231";
		
		boolean outsideOfRange = dateUtility.isOutsideOfRange(date, notBefore, notAfter);
		assertTrue("Date " + date + " is outside of dates notBefore[" + notBefore + "] notAfter[" + notAfter + "]", 
				outsideOfRange);
		
	}
	
	@Test
	public void ageCalculationTest() {
		DateTime today = new DateTime();
		DateTime eighteenYearsAgo = today.minusYears(18);
		DateTime seventeenYearsAgo = eighteenYearsAgo.plusDays(1);
		DateTime nullDate = null;
//		System.out.println("Age 18 string: " + eighteenYearsAgo);
//		System.out.println("Age 17 string: " + seventeenYearsAgo);
		
		assertEquals("should calculate to 18", 18, dateUtility.getAge(eighteenYearsAgo.toDate()));
		assertEquals("Should calculate to 17", 17, dateUtility.getAge(seventeenYearsAgo.toDate()));
		assertEquals("should be zero if born today", 0, dateUtility.getAge(today.toDate()));
		DateTime dt = null;
		assertEquals("Should be -1 if it's a null input", -1, dateUtility.getAge(dt));
		
		int superLongYears = 18923;
		assertEquals("Should be able to handle a very old age", superLongYears, dateUtility.getAge(today.minusYears(superLongYears).toDate()));
	}
	
	@Test
	public void adultAgeCalculationTest() {
		DateTime today = new DateTime();
		DateTime eighteenYearsAgo = today.minusYears(18);
		DateTime seventeenYearsAgo = eighteenYearsAgo.plusDays(1);
		assertTrue("should be adult", dateUtility.isAdult(eighteenYearsAgo.toDate()));
		assertFalse("should not be an adult if null is sent", dateUtility.isAdult(null));
		assertFalse("Should not be an adult if today is birthday", dateUtility.isAdult(new Date()));
		assertFalse("Should not be an adult if age 17", dateUtility.isAdult(seventeenYearsAgo.toDate()));
	}
	
	@Test
	public void nistFormat() {
		System.out.println("expected format: " + dateUtility.toFullNistString(new DateTime()));
		
		
		String fullDateWithTimezone = "20150624073733.994-0500";
		String partialDateNoMillisWithTimezone = "20150624073733-0500";

		Date d1z = dateUtility.parseDate(fullDateWithTimezone);
		Date d2z = dateUtility.parseDate(partialDateNoMillisWithTimezone);
		
		assertNotNull("shouldn't be null - d1z", d1z);
		assertNotNull("shouldn't be null - d2z", d2z);
	}

	@Test
	public void mcirFormat() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String fullDateWithTimezone = "20180903131228-0500";
		DateTime d1z = dateUtility.parseDateTime(fullDateWithTimezone);
		assertNotNull("shouldn't be null - d1z", d1z);
	}


	@Test
	public void wierdFormat() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String fullDateWithTimezoneShort = "20180903131228-500";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(fullDateWithTimezoneShort);
		assertNull("should be null - the timezone is an unkonwn format", d1z);
	}


	@Test
	public void subseconds1() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.1-0500";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a tenth of a second", d1z);
	}


	@Test
	public void subseconds2() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.12-0500";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a hundredth of a second", d1z);
	}


	@Test
	public void subseconds3() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.123-0500";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a thousanth of a second", d1z);
	}


	@Test
	public void subseconds4() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.1234-0500";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a ten thousanth of a second", d1z);
	}

	@Test
	public void subseconds1noTZ() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.1";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a tenth of a second", d1z);
	}


	@Test
	public void subseconds2noTZ() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.12";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a hundredth of a second", d1z);
	}


	@Test
	public void subseconds3noTZ() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.123";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a thousanth of a second", d1z);
	}


	@Test
	public void subseconds4noTZ() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228.1234";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a ten thousanth of a second", d1z);
	}

	@Test
	public void nosubseconds() {
		System.out.println("expected format: " + dateUtility.toString(new DateTime()));
		String subsecondTime = "20180903131228";
		//expected format: YYYYMMDDHHMMSS+/-ZZZZ
		//notice there are four zeros. the test string has three.  it should fail.
		//this is consistent with CDC implementation guide:
		//Format: YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]
		DateTime d1z = dateUtility.parseDateTime(subsecondTime);
		assertNotNull("should NOT be null - should accept a ten thousanth of a second", d1z);
	}
}
