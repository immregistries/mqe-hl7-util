package org.immregistries.dqa.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.joda.time.DateTime;
import org.junit.Test;

public class DateUtilityTest {
	DateUtility datr = DateUtility.INSTANCE;

	@Test
	public void testGetAgeFromDates() {
		Date today = new Date();
		today.setYear(2018);
		today.setMinutes(1);
		Date yesterYear = new Date();
		yesterYear.setYear(2000);
		yesterYear.setMinutes(0);
		int age = datr.getYearsBetween(yesterYear, today);

		assertEquals("should be 18 years between", 18, age);
	}
	
	@Test
	public void testStringParsing() {
		String notADate = "saflj";
		
		assertFalse("it's garbage", datr.isDate(notADate));
		
		String isADate = "20160101";
		assertTrue("it's good, really[" + isADate + "]", datr.isDate(isADate));
//		System.out.println(datr.parseDate(isADate));
		isADate = "2016-01-01";
		assertFalse("it's bad, really[" + isADate + "]", datr.isDate(isADate));
//		System.out.println(datr.parseDate(isADate));
		
		isADate = "19000101";
		assertTrue("it's good, really[" + isADate + "]", datr.isDate(isADate));
//		System.out.println(datr.parseDate(isADate));
		
	}
	@Test
	public void emptyDateParsing() {
		String notADate = "";
		
		assertFalse("it's emptyString", datr.isDate(notADate));
		
		String nullDate = null;
		
		assertFalse("it's null, really[" + nullDate + "]", datr.isDate(nullDate));
		 
	}
	
	@Test
	public void beforeOrAfterTesting1() {
		String date = "20160101";
		String notBefore = "20000101";
		String notAfter  = "20161231";
		
		boolean beforeOrAfter = datr.isOutsideOfRange(date, notBefore, notAfter);
		assertFalse("Date " + date + " is within dates notBefore[" + notBefore + "] notAfter[" + notAfter + "]", 
				beforeOrAfter);
		
		date = "20200101";
		beforeOrAfter = datr.isOutsideOfRange(date, notBefore, notAfter);
		assertTrue("Date " + date + " is NOT within dates notBefore[" + notBefore + "] notAfter[" + notAfter + "]", 
				beforeOrAfter);
	}
	
	@Test
	public void beforeOrAfterTesting2() {
		String date = "20160101";
		String notBefore = null;
		String notAfter  = "20121231";
		
		boolean outsideOfRange = datr.isOutsideOfRange(date, notBefore, notAfter);
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
		
		assertEquals("should calculate to 18", 18, datr.getAge(eighteenYearsAgo.toDate()));
		assertEquals("Should calculate to 17", 17, datr.getAge(seventeenYearsAgo.toDate()));
		assertEquals("should be zero if born today", 0, datr.getAge(today.toDate()));
		DateTime dt = null;
		assertEquals("Should be -1 if it's a null input", -1, datr.getAge(dt));
		
		int superLongYears = 18923;
		assertEquals("Should be able to handle a very old age", superLongYears, datr.getAge(today.minusYears(superLongYears).toDate()));
	}
	
	@Test
	public void adultAgeCalculationTest() {
		DateTime today = new DateTime();
		DateTime eighteenYearsAgo = today.minusYears(18);
		DateTime seventeenYearsAgo = eighteenYearsAgo.plusDays(1);
		assertTrue("should be adult", datr.isAdult(eighteenYearsAgo.toDate()));
		assertFalse("should not be an adult if null is sent", datr.isAdult(null));
		assertFalse("Should not be an adult if today is birthday", datr.isAdult(new Date()));
		assertFalse("Should not be an adult if age 17", datr.isAdult(seventeenYearsAgo.toDate()));
	}
	
	@Test
	public void nistFormat() {
		System.out.println("expected format: " + datr.toFullNistString(new DateTime()));
		
		
		String fullDateWithTimezone = "20150624073733.994-0500";
		String partialDateNoMillisWithTimezone = "20150624073733-0500";

		Date d1z = datr.parseDate(fullDateWithTimezone);
		Date d2z = datr.parseDate(partialDateNoMillisWithTimezone);
		
		assertNotNull("shouldn't be null - d1z", d1z);
		assertNotNull("shouldn't be null - d2z", d2z);
	}
}
