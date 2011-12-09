package com.lm.test;

import java.util.Collection;


import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//import com.google.appengine.api.datastore.dev.LocalDatastoreService;
//import com.google.appengine.tools.development.ApiProxyLocalImpl;
//import com.google.apphosting.api.ApiProxy;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.lm.keyrow.DefaultCondition;
import com.lm.keyrow.EqualCondition;
import com.lm.keyrow.KR;
import com.lm.keyrow.KeyRowUtils;

public class KeyRowTest {
	private static LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	static {
		/*
		ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
		ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")) {});
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
		proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
		*/
		
		helper.setUp();
	}
	
	@Before
    public void setUp() {
        //helper.setUp();
    }

    @After
    public void tearDown() {
        //helper.tearDown();
    }

	@After
	public void after() {
        Logger.getLogger("DataNucleus.Plugin").setLevel(Level.OFF);
		Logger.getLogger("DataNucleus.Persistence").setLevel(Level.OFF);
		
		/*
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
		LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService("datastore_v3");
		datastoreService.clearProfiles();
		// not strictly necessary to null these out but there's no harm either
		ApiProxy.setDelegate(null);
		ApiProxy.setEnvironmentForCurrentThread(null);
		*/
	}
	
	@Test
	public void testImpl() throws Exception {
		KeyRowImpl kr = new KeyRowImpl();
		kr.setKey("This is key");
		kr.setValue1("Value1 is here");
		kr.setValue2(100);
		
		KR.insert(kr);
		Assert.assertEquals(1, KR.size(KeyRowImpl.class));
		
		KeyRowImpl kr_get = (KeyRowImpl) KR.get(KeyRowImpl.class, kr.key());
		Assert.assertTrue(kr.equals(kr_get));
	}
	
	@Test
	public void testImpl2() throws Exception {
		String key = "This is key";
		KeyRowImpl kr;
		kr = (KeyRowImpl) KR.get(KeyRowImpl.class, key);
		Assert.assertEquals("Value1 is here", kr.getValue1());
		Assert.assertEquals(new Integer(100), kr.getValue2());
	}
	
	@Test
	public void testAnnt() throws Exception {
		AnntKeyRow kr = new AnntKeyRow();
		kr.setKey("This is key! #" + 0);
		kr.setValue1("Value1 is here!");
		kr.setValue2(new Date().getTime());
		
		KR.insert(kr);
		Assert.assertEquals(1, KR.size(AnntKeyRow.class));
		
		AnntKeyRow kr_get = (AnntKeyRow) KR.get(AnntKeyRow.class, KeyRowUtils.getKeyValue(kr));
		Assert.assertTrue(kr.equals(kr_get));
	}
	
	@Test
	public void testSelect() throws Exception {
		AnntKeyRow kr0 = null;
		AnntKeyRow kr1 = null;
		Date curr = new Date();
		for (int i=0; i<10; i++) {
			AnntKeyRow kr = new AnntKeyRow();
			kr.setKey("This is key! #" + i);
			kr.setValue1("Value1 is here! #" + (i%2));
			kr.setValue2(curr.getTime());
			
			boolean r = KR.insert(kr);
			
			if (i == 0) {
				kr0 = kr;
				Assert.assertFalse(r);
			}
			else if (i == 1) {
				kr1 = kr;
				Assert.assertTrue(r);
			}
			else {
				Assert.assertTrue(r);
			}
		}
		
		Assert.assertEquals(10, KR.size(AnntKeyRow.class));
		
		AnntKeyRow kr_get0 = (AnntKeyRow) KR.get(AnntKeyRow.class, KeyRowUtils.getKeyValue(kr0));
		Assert.assertFalse(kr0.equals(kr_get0));
		AnntKeyRow kr_get1 = (AnntKeyRow) KR.get(AnntKeyRow.class, KeyRowUtils.getKeyValue(kr1));
		Assert.assertTrue(kr1.equals(kr_get1));
		
		AnntKeyRow kr = new AnntKeyRow();
		kr.setValue1("Value1 is here! #0");
		EqualCondition condition1 = new EqualCondition(kr, 0, 10);
		Assert.assertEquals(4, KR.size(AnntKeyRow.class, condition1));
		Collection<Object> kr_select = (Collection<Object>) KR.query(AnntKeyRow.class, condition1);
		Assert.assertEquals(4, kr_select.size());
		
		AnntKeyRow kr4del = new AnntKeyRow();
		kr4del.setValue1("Value1 is here! #1");
		Assert.assertEquals(5, KR.remove(AnntKeyRow.class, new EqualCondition<AnntKeyRow>(kr4del)));
		Assert.assertEquals(5, KR.size(AnntKeyRow.class));
		Assert.assertEquals(4, KR.remove(AnntKeyRow.class, new EqualCondition<AnntKeyRow>(kr)));
		
		DefaultCondition condition2 = new DefaultCondition(0, 10);
		Collection<Object> kr_all = KR.query(AnntKeyRow.class, condition2);
		AnntKeyRow kr4clear = new AnntKeyRow();
		kr4clear.setValue2((Long) KeyRowUtils.getValue(kr_all.iterator().next(), "Value2"));
		Assert.assertEquals(1, KR.remove(AnntKeyRow.class, new EqualCondition<AnntKeyRow>(kr4clear)));
		Assert.assertEquals(0, KR.size(AnntKeyRow.class));
	}
	
	@Test
	public void testUpdate() throws Exception {
		AnntKeyRow kr = new AnntKeyRow();
		kr.setKey("This is key! #" + 0 + " for update!");
		kr.setValue1("Value1 is here!");
		kr.setValue2(new Date().getTime());
		
		KR.insert(kr);
		kr.setValue1("Value1 is here! modified!");
		KR.update(kr);
		
		AnntKeyRow get = (AnntKeyRow) KR.get(AnntKeyRow.class, kr.getKey());
		Assert.assertEquals(kr.getValue1(), get.getValue1());
		Assert.assertEquals(kr.getValue2(), get.getValue2());
		
		KR.insert(kr);
		kr.setValue1("Value1 is here! modified2!");
		kr.setValue2(null);
		KR.update(kr);
		
		AnntKeyRow get2 = (AnntKeyRow) KR.get(AnntKeyRow.class, kr.getKey());
		Assert.assertEquals(kr.getValue1(), get2.getValue1());
		Assert.assertNotNull(get2.getValue2());
		
		KR.remove(AnntKeyRow.class, kr.getKey());
	}
	
	@Test
	public void testMap() throws Exception {
		AnntKeyRow kr = new AnntKeyRow();
		kr.setKey("This is key! #" + 0);
		kr.setValue1("Value1 is here!");
		
		KR.insert(kr);
		Assert.assertEquals(1, KR.size(AnntKeyRow.class));
		
		Map<String, Object> map = KeyRowUtils.toMap(kr);
		Assert.assertEquals(KeyRowUtils.getKeyValue(kr), map.get("Key"));
		Assert.assertEquals(kr.getValue1(), map.get("Value1"));
		
		String key2 = "This is key2!";
		map.put(KeyRowUtils.getKeyName(kr), key2);
		KeyRowUtils.mapTo(map, kr);
		Assert.assertEquals(key2, kr.getKey());
	}
	
	@Test
	public void test2Map() throws Exception {
		String key = "KEYKEY";
		AnntKeyRow kr = new AnntKeyRow();
		kr.setKey(key);
		kr.setValue1("Value1");
		
		Annt2KeyRow kr2 = new Annt2KeyRow();
		kr2.setKey(key);
		kr2.setValue1("Value2");
		
		KR.insert(kr);
		KR.insert(kr2);
		
		AnntKeyRow krg = (AnntKeyRow) KR.get(AnntKeyRow.class, key);
		Annt2KeyRow kr2g = (Annt2KeyRow) KR.get(Annt2KeyRow.class, key);
		
		Assert.assertEquals(kr.getValue1(), krg.getValue1());
		Assert.assertEquals(kr2.getValue1(), kr2g.getValue1());
		
		krg = KR.getAccessorT(kr).get(kr.getClass().getSimpleName(), key);
		kr2g = KR.getAccessorT(kr2).get(kr2.getClass().getSimpleName(), key);

		Assert.assertEquals(kr.getValue1(), krg.getValue1());
		Assert.assertEquals(kr2.getValue1(), kr2g.getValue1());
	}
}
