package cn.hutool.core.text;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.regex.Pattern;

public class CharSequenceUtilTest {

	@Test
	public void replaceTest() {
		final String actual = CharSequenceUtil.replace("SSM15930297701BeryAllen", Pattern.compile("[0-9]"), matcher -> "");
		Assert.assertEquals("SSMBeryAllen", actual);
	}

	@Test
	public void replaceTest2() {
		// https://gitee.com/dromara/hutool/issues/I4M16G
		final String replace = "#{A}";
		final String result = CharSequenceUtil.replace(replace, "#{AAAAAAA}", "1");
		Assert.assertEquals(replace, result);
	}

	@Test
	public void replaceByStrTest() {
		final String replace = "SSM15930297701BeryAllen";
		final String result = CharSequenceUtil.replace(replace, 5, 12, "***");
		Assert.assertEquals("SSM15***01BeryAllen", result);

		final String emoji = StrUtil.replace("\uD83D\uDE00aabb\uD83D\uDE00ccdd", 2, 6, "***");
		Assert.assertEquals("\uD83D\uDE00a***ccdd", emoji);
	}

	@Test
	public void addPrefixIfNotTest() {
		final String str = "hutool";
		String result = CharSequenceUtil.addPrefixIfNot(str, "hu");
		Assert.assertEquals(str, result);

		result = CharSequenceUtil.addPrefixIfNot(str, "Good");
		Assert.assertEquals("Good" + str, result);
	}

	@Test
	public void addSuffixIfNotTest() {
		final String str = "hutool";
		String result = CharSequenceUtil.addSuffixIfNot(str, "tool");
		Assert.assertEquals(str, result);

		result = CharSequenceUtil.addSuffixIfNot(str, " is Good");
		Assert.assertEquals(str + " is Good", result);

		// https://gitee.com/dromara/hutool/issues/I4NS0F
		result = CharSequenceUtil.addSuffixIfNot("", "/");
		Assert.assertEquals("/", result);
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Test
	public void normalizeTest() {
		// https://blog.csdn.net/oscar999/article/details/105326270

		String str1 = "\u00C1";
		String str2 = "\u0041\u0301";

		Assert.assertNotEquals(str1, str2);

		str1 = CharSequenceUtil.normalize(str1);
		str2 = CharSequenceUtil.normalize(str2);
		Assert.assertEquals(str1, str2);
	}

	@Test
	public void indexOfTest() {
		int index = CharSequenceUtil.indexOf("abc123", '1');
		Assert.assertEquals(3, index);
		index = CharSequenceUtil.indexOf("abc123", '3');
		Assert.assertEquals(5, index);
		index = CharSequenceUtil.indexOf("abc123", 'a');
		Assert.assertEquals(0, index);
	}

	@Test
	public void indexOfTest2() {
		int index = CharSequenceUtil.indexOf("abc123", '1', 0, 3);
		Assert.assertEquals(-1, index);

		index = CharSequenceUtil.indexOf("abc123", 'b', 0, 3);
		Assert.assertEquals(1, index);
	}

	@Test
	public void subPreGbkTest() {
		// https://gitee.com/dromara/hutool/issues/I4JO2E
		final String s = "华硕K42Intel酷睿i31代2G以下独立显卡不含机械硬盘固态硬盘120GB-192GB4GB-6GB";

		String v = CharSequenceUtil.subPreGbk(s, 40, false);
		Assert.assertEquals(39, v.getBytes(CharsetUtil.GBK).length);

		v = CharSequenceUtil.subPreGbk(s, 40, true);
		Assert.assertEquals(41, v.getBytes(CharsetUtil.GBK).length);
	}

	@Test
	public void startWithTest() {
		// https://gitee.com/dromara/hutool/issues/I4MV7Q
		Assert.assertFalse(CharSequenceUtil.startWith("123", "123", false, true));
		Assert.assertFalse(CharSequenceUtil.startWith(null, null, false, true));
		Assert.assertFalse(CharSequenceUtil.startWith("abc", "abc", true, true));

		Assert.assertTrue(CharSequenceUtil.startWithIgnoreCase(null, null));
		Assert.assertFalse(CharSequenceUtil.startWithIgnoreCase(null, "abc"));
		Assert.assertFalse(CharSequenceUtil.startWithIgnoreCase("abcdef", null));
		Assert.assertTrue(CharSequenceUtil.startWithIgnoreCase("abcdef", "abc"));
		Assert.assertTrue(CharSequenceUtil.startWithIgnoreCase("ABCDEF", "abc"));
	}

	@Test
	public void endWithTest() {
		Assert.assertFalse(CharSequenceUtil.endWith("123", "123", false, true));
		Assert.assertFalse(CharSequenceUtil.endWith(null, null, false, true));
		Assert.assertFalse(CharSequenceUtil.endWith("abc", "abc", true, true));

		Assert.assertTrue(CharSequenceUtil.endWithIgnoreCase(null, null));
		Assert.assertFalse(CharSequenceUtil.endWithIgnoreCase(null, "abc"));
		Assert.assertFalse(CharSequenceUtil.endWithIgnoreCase("abcdef", null));
		Assert.assertTrue(CharSequenceUtil.endWithIgnoreCase("abcdef", "def"));
		Assert.assertTrue(CharSequenceUtil.endWithIgnoreCase("ABCDEF", "def"));
	}

	@Test
	public void removePrefixIgnoreCaseTest(){
		Assert.assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "abc"));
		Assert.assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABC"));
		Assert.assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "Abc"));
		Assert.assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", ""));
		Assert.assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", null));
		Assert.assertEquals("", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCde"));
		Assert.assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCdef"));
		Assert.assertNull(CharSequenceUtil.removePrefixIgnoreCase(null, "ABCdef"));
	}

	@Test
	public void removeSuffixIgnoreCaseTest(){
		Assert.assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "cde"));
		Assert.assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "CDE"));
		Assert.assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "Cde"));
		Assert.assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", ""));
		Assert.assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", null));
		Assert.assertEquals("", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCde"));
		Assert.assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCdef"));
		Assert.assertNull(CharSequenceUtil.removeSuffixIgnoreCase(null, "ABCdef"));
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void trimToNullTest(){
		String a = "  ";
		Assert.assertNull(CharSequenceUtil.trimToNull(a));

		a = "";
		Assert.assertNull(CharSequenceUtil.trimToNull(a));

		a = null;
		Assert.assertNull(CharSequenceUtil.trimToNull(a));
	}

	@Test
	public void containsAllTest() {
		final String a = "2142342422423423";
		Assert.assertTrue(StrUtil.containsAll(a, "214", "234"));
	}

	@Test
	public void defaultIfEmptyTest() {
		final String emptyValue = "";
		final Instant result1 = CharSequenceUtil.defaultIfEmpty(emptyValue,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assert.assertNotNull(result1);

		final String dateStr = "2020-10-23 15:12:30";
		final Instant result2 = CharSequenceUtil.defaultIfEmpty(dateStr,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assert.assertNotNull(result2);
	}

	@Test
	public void defaultIfBlankTest() {
		final String emptyValue = " ";
		final Instant result1 = CharSequenceUtil.defaultIfBlank(emptyValue,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assert.assertNotNull(result1);

		final String dateStr = "2020-10-23 15:12:30";
		final Instant result2 = CharSequenceUtil.defaultIfBlank(dateStr,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assert.assertNotNull(result2);
	}

	@Test
	public void replaceLastTest() {
		final String str = "i am jack and jack";
		final String result = StrUtil.replaceLast(str, "JACK", null, true);
		Assert.assertEquals(result, "i am jack and ");
	}

	@Test
	public void replaceFirstTest() {
		final String str = "yes and yes i do";
		final String result = StrUtil.replaceFirst(str, "YES", "", true);
		Assert.assertEquals(result, " and yes i do");
	}

	@Test
	public void issueI5YN49Test() {
		final String str = "A5E6005700000000000000000000000000000000000000090D0100000000000001003830";
		Assert.assertEquals("38", StrUtil.subByLength(str,-2,2));
	}

	@Test
	public void commonPrefixTest() {

		// -------------------------- None match -----------------------

		Assert.assertEquals("", CharSequenceUtil.commonPrefix("", "abc"));
		Assert.assertEquals("", CharSequenceUtil.commonPrefix(null, "abc"));
		Assert.assertEquals("", CharSequenceUtil.commonPrefix("abc", null));
		Assert.assertEquals("", CharSequenceUtil.commonPrefix("abc", ""));

		Assert.assertEquals("", CharSequenceUtil.commonPrefix("azzzj", "bzzzj"));

		Assert.assertEquals("", CharSequenceUtil.commonPrefix("english中文", "french中文"));

		// -------------------------- Matched -----------------------

		Assert.assertEquals("name_", CharSequenceUtil.commonPrefix("name_abc", "name_efg"));

		Assert.assertEquals("zzzj", CharSequenceUtil.commonPrefix("zzzja", "zzzjb"));

		Assert.assertEquals("中文", CharSequenceUtil.commonPrefix("中文english", "中文french"));

	}

	@Test
	public void commonSuffixTest() {

		// -------------------------- None match -----------------------

		Assert.assertEquals("", CharSequenceUtil.commonSuffix("", "abc"));
		Assert.assertEquals("", CharSequenceUtil.commonSuffix(null, "abc"));
		Assert.assertEquals("", CharSequenceUtil.commonSuffix("abc", null));
		Assert.assertEquals("", CharSequenceUtil.commonSuffix("abc", ""));

		Assert.assertEquals("", CharSequenceUtil.commonSuffix("zzzja", "zzzjb"));

		Assert.assertEquals("", CharSequenceUtil.commonSuffix("中文english", "中文Korean"));

		// -------------------------- Matched -----------------------

		Assert.assertEquals("_name", CharSequenceUtil.commonSuffix("abc_name", "efg_name"));

		Assert.assertEquals("zzzj", CharSequenceUtil.commonSuffix("abczzzj", "efgzzzj"));

		Assert.assertEquals("中文", CharSequenceUtil.commonSuffix("english中文", "Korean中文"));

	}
}
