package cn.hutool.json.xml;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

public class XMLTest {

	@Test
	public void toXmlTest(){
		final JSONObject put = JSONUtil.ofObj()
				.set("aaa", "你好")
				.set("键2", "test");
		final String s = JSONUtil.toXmlStr(put);
		MatcherAssert.assertThat(s, CoreMatchers.anyOf(CoreMatchers.is("<aaa>你好</aaa><键2>test</键2>"), CoreMatchers.is("<键2>test</键2><aaa>你好</aaa>")));
	}

	@Test
	public void escapeTest(){
		final String xml = "<a>•</a>";
		final JSONObject jsonObject = JSONXMLUtil.toJSONObject(xml);

		Assert.assertEquals("{\"a\":\"•\"}", jsonObject.toString());

		final String xml2 = JSONXMLUtil.toXml(JSONUtil.parseObj(jsonObject));
		Assert.assertEquals(xml, xml2);
	}

	@Test
	public void xmlContentTest(){
		final JSONObject jsonObject = JSONUtil.ofObj().set("content","123456");

		String xml = JSONXMLUtil.toXml(jsonObject);
		Assert.assertEquals("123456", xml);

		xml = JSONXMLUtil.toXml(jsonObject, null, new String[0]);
		Assert.assertEquals("<content>123456</content>", xml);
	}
}
