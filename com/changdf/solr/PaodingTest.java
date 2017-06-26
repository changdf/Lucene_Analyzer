package com.changdf.solr;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class PaodingTest {

	public static void main(String[] args) throws Exception {
		String compileDicHome = "C:\\Users\\changdf\\Desktop\\solr-6.5.1\\server\\resources\\dic\\.compiled";
		//deleteFile(new File(compileDicHome));
		
		String input = "已婚，最终幻想常东锋我是一个咸鱼i am '我是咸鱼'《莎士比亚》changdf2210992483@qq.com13716183704常东锋奶爸。中国大陆愤青。乐观的抑郁症患者，间歇性精神正常。重度互联网使用者。国产汽车音响后级山寨者。手机迷。粗野、没文化，但一般不骂人";
		Reader reader = new StringReader(input);
		PaodingAnalyzer analyzer = new PaodingAnalyzer("classpath:paoding-analysis.properties");
        TokenStream tokenStream = analyzer.tokenStream("", reader);
        tokenStream.reset();
		CharTermAttribute termAtt = tokenStream.getAttribute(CharTermAttribute.class);
		StringBuffer sb = new StringBuffer();
		while (tokenStream.incrementToken()) {
			sb.append(termAtt.toString()+",");
		}
		tokenStream.end();
		System.out.println(sb.toString());
	}
	
	private static void deleteFile(File file) {
		if (file.exists()) { //判断文件是否存在  
			if (file.isFile()) { //判断是否是文件  
				file.delete(); //删除文件   
			} else if (file.isDirectory()) { //否则如果它是一个目录  
				File[] files = file.listFiles(); //声明目录下所有的文件 files[];  
				for (int i = 0; i < files.length; i++) { //遍历目录下所有的文件  
					deleteFile(files[i]); //把每个文件用这个方法进行迭代  
				}
				file.delete(); //删除文件夹  
			}
		} else {
			System.out.println("所删除的文件不存在");
		}
	}
}
