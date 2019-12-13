package com.happytrip.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import com.happytrip.model.Airline;
import com.happytrip.model.Flight;
import com.happytrip.model.Route;

public final class StringUtil {

	private StringUtil() {
	}

	public static long generateReference(long seed) {
		Random random = new Random(seed);
		long val = random.nextLong();
		if (val < 0) {
			return val * -1;
		}
		return val;
	}

	public static Properties getPropertiesFromClasspath(String propFileName)
			throws IOException {
		// loading xmlProfileGen.properties from the classpath
		Properties props = new Properties();
		InputStream inputStream = StringUtil.class.getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream == null) {
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}

		props.load(inputStream);

		return props;
	}

	public static StringBuffer getAboutUsPageContent(String fileName)
			throws IOException {
		byte[] byteArray = new byte[200];
		InputStream inputStream = getStream(byteArray, fileName);
		StringBuffer stringBuffer = new StringBuffer();
		int numOfBytesRead = 0;
		while ((numOfBytesRead = inputStream.read(byteArray)) > 0) {
			stringBuffer = stringBuffer.append(String.valueOf(byteArray));
		}
		return stringBuffer;
	}

	private static InputStream getStream(byte[] byteArray, String fileName)
			throws IOException {
		InputStream inputStream = (InputStream) StringUtil.class
				.getClassLoader().getResourceAsStream(fileName);

		return inputStream;

	}

	public static StringBuffer getExternalAdvertisementPageContent(
			String fileName) throws IOException {
		byte[] byteArray = new byte[20];
		InputStream inputStream = getStream(byteArray, fileName);
		StringBuffer stringBuffer = new StringBuffer();
		int numOfBytesRead = 0;
		while ((numOfBytesRead = inputStream.read(byteArray)) > 0) {
			stringBuffer = stringBuffer.append(new String(byteArray));
		}
		return stringBuffer;
	}

	/*
	 * code modified for File Handling_CR_07.1
	 */
	// modified code starts here for File Handling_CR_07.1
	public static List<String> getAllString(String delimiter, String fileName)
			throws IOException {
		InputStream inStream = StringUtil.class.getClassLoader()
				.getResourceAsStream(fileName);
		InputStreamReader inputStreamReader = new InputStreamReader(inStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		List<String> words = new ArrayList<String>();
		String value;
		try {
			value = bufferedReader.readLine();
			while (value != null) {
				String[] values = value.split(delimiter);
				for (String str : values) {
					words.add(str);
				}
				value = bufferedReader.readLine();
			}
		} catch (IOException e) {

		}
		return words;
	}

	// modified code ends here for File Handling_CR_07.1

	public static  void writeAirlinesToTextFile(List<Airline> airlines, String fileName)
			throws IOException {
		System.out.println("in StringUtil writeToTextFile(List<Airline> airlines, String fileName)");
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		for(Airline airline:airlines){
			writer.println(airline.getAirlineName());
		}
		writer.close();
		
	}
	public static void main(String[] args) throws IOException {

		System.out.println(StringUtil.getAllString(" ",
				"data/abusivewords.txt").size());
	}
}
