package com.example.utills;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import au.com.bytecode.opencsv.CSVReader;

import com.example.model.DataModel;

public class Utilss {

	// get data from excel sheet
	public static void getExceldata(String CSV_location_path,
			ArrayList<DataModel> arrayList) throws IOException {
		CSVReader csvReader = new CSVReader(new FileReader(CSV_location_path));
		String[] row = null;
		while ((row = csvReader.readNext()) != null) {

			String keywords = row[0].trim();
			System.out.println(keywords);

			DataModel model = new DataModel();

			model.setKeyword(keywords);
			arrayList.add(model);

		}
		// ...

		csvReader.close();

	}

	public static void getDataFromNotepad(String note_file_path,
			ArrayList<DataModel> arrayList) throws IOException {
		String keywords;
		FileReader fr = new FileReader(note_file_path);
		BufferedReader br = new BufferedReader(fr);
		// /read line from the file upto null
		while ((keywords = br.readLine()) != null) {
			DataModel model = new DataModel();
			model.setKeyword(keywords);
			arrayList.add(model);
			System.out.println("From TextFile=" + keywords);
		}
		br.close();
	}

	public static void getPeopleNameFromNotepad(String note_file_path,
			ArrayList<String> arrayList) throws IOException {
		String keywords;
		FileReader fr = new FileReader(note_file_path);
		BufferedReader br = new BufferedReader(fr);
		// /read line from the file upto null
		while ((keywords = br.readLine()) != null) {

			arrayList.add(keywords);
			System.out.println("From TextFile=" + keywords);
		}
		br.close();
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static String getbody(String bodypart, String name) {

		// String body1="Hello";
		String body = "Hey,I am recruiting for Amazon (IT Directors,Managers,Sales Executive,Freelancer Etc.) and your profile looks fit for the job role which am hiring for. Would you be interested for an interview? If yes,please do apply via this link - "
				+ bodypart
				+ "                                                                                                                                                                                                                                  (Link Valid For U.S. Users Only)                                                                                                                                                                                         You need to register with in the system and create your profile and apply for the job code AZ-345898"
				+ "                                                                                                                                                                                                                              If you do not find this opportunity interesting please ignore this email."
				+ "                                                                                                                                                             Thank you, "
				+ name;

		System.out.println("body  " + body);

		return body;

	}

	public static void writeDataInText(String path, String data)
			throws FileNotFoundException {
		try {

			File file = new File(path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("\r\n");
			bw.write(data);

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
