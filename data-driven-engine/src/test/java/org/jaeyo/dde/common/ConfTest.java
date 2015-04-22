package org.jaeyo.dde.common;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class ConfTest {

	@Test
	public void test() {
		Conf.set("test", 1);
		int value = Conf.getAs("test");
		assertTrue(value == 1);
		
		Conf.set("testLongValue", 2L);
		long longValue = Conf.getAs("testLongValue");
		assertTrue(longValue == 2L);
		
		List<String> list = Lists.newArrayList();
		Conf.set("testListObject", list);
		List<String> storedList = Conf.getAs("testListObject");
		assertEquals(list, storedList);
		
		Conf.set("testLongValue3", 3L);
		assertTrue(3L == Conf.getAs("testLongValue3", Long.class));
	}
}