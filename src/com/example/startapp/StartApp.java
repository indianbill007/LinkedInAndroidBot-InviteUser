package com.example.startapp;

import io.selendroid.client.SelendroidDriver;
import io.selendroid.client.SelendroidKeys;
import io.selendroid.common.SelendroidCapabilities;
import io.selendroid.standalone.SelendroidConfiguration;
import io.selendroid.standalone.SelendroidLauncher;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;

import com.example.model.DataModel;
import com.example.utills.ConstantID;
import com.example.utills.Utilss;

public class StartApp {

	static boolean isSearchPresent;
	private static SelendroidLauncher selendroidServer = null;
	private static WebDriver driver = null;
	static WebElement add_button;

	// static String csvfilepath = "assets/data.csv";
	static String notefilepath = "assets/data.txt";
	static ArrayList<DataModel> datalist = new ArrayList<DataModel>();

	static int invite_present_count = 0;
	static int invite_not_present_count = 0;
	static int csv_index = 0;
	static WebElement search_field_editbox;

	static String usercsv = "assets/user.txt";

	static ArrayList<String> person_list = new ArrayList<String>();

	public static void main(String[] args) throws Exception {

		// Get all the keyword from excel to runtime list

		// if data is in csv
		// Utilss.getExceldata(csvfilepath, datalist);

		// if data is in notepad
		Utilss.getDataFromNotepad(notefilepath, datalist);

		Utilss.getPeopleNameFromNotepad(usercsv, person_list);

		SelendroidConfiguration config = new SelendroidConfiguration();
		config.addSupportedApp("apk/linkedin.apk");
		config.setSessionTimeoutSeconds(60 * 60 * 24); // one day, change to
														// what you want it to
														// be.

		selendroidServer = new SelendroidLauncher(config);

		selendroidServer.launchSelendroid();

		URL url = new URL("http://localhost:4444/wd/hub");
		// -------------------------------------------------------
		SelendroidCapabilities linkedin = SelendroidCapabilities
				.device("com.linkedin.android:3.5");

		driver = new SelendroidDriver(url, linkedin);

		try {

			Thread.sleep(7000);
			driver.findElement(By.id(ConstantID.Button_search)).click();

			// set first search keyword
			Set_Search_keyword(datalist.get(0).getKeyword(), driver);

			isSearchPresent = true;
			Thread.sleep(7000);

			// one element width= 50dp

			System.out.println(">>>>>> Iterater is initialised <<<<<");

			for (int i = 0; i < ConstantID.iterate_time; i++) {

				Invite_User();

				Thread.sleep(10000);

				Scroll();

				if (csv_index >= datalist.size()) {
					System.out.println(">>>>>> END <<<<<");
					break;
				}
			}

		} catch (Exception e) {
			isSearchPresent = false;

			e.printStackTrace();

		}

		if (isSearchPresent == false) {
			try {
				Thread.sleep(7000);
				driver.findElement(By.id(ConstantID.SignIn_ID)).click();
				Thread.sleep(7000);
				driver.findElement(By.id(ConstantID.Username_ID)).sendKeys(
						ConstantID.UserName);
				driver.findElement(By.id(ConstantID.Password_ID)).sendKeys(
						ConstantID.PassWord);

				driver.findElement(By.id(ConstantID.Button_SignIN_ID)).click();

				Thread.sleep(5000);
				driver.findElement(By.xpath(ConstantID.Action_bar_Xpath))
						.click();

				// new Actions(driver).sendKeys(SelendroidKeys.BACK).perform();
				// new Actions(driver).sendKeys(SelendroidKeys.BACK).perform();

				driver.findElement(By.id(ConstantID.Button_search)).click();
				new Actions(driver).sendKeys(SelendroidKeys.BACK).perform();
				new Actions(driver).sendKeys(SelendroidKeys.BACK).perform();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	/*
	 * public static void Invite_User() { for (int i = 1; i <=
	 * ConstantID.list_size; i++) {
	 * 
	 * String button_xpath = "//ListView/RelativeLayout[" + i +
	 * "]/FrameLayout/AnimatedActionImageView";
	 * 
	 * add_button = driver.findElement(By.xpath(button_xpath));
	 * 
	 * if (add_button.isDisplayed() == true) { add_button.click();
	 * invite_present_count++;
	 * 
	 * System.out .println("Total Invite Count=" + invite_present_count);
	 * invite_not_present_count = 0; } else { invite_not_present_count++; }
	 * 
	 * if (invite_not_present_count > 500) { invite_not_present_count = 0;
	 * System.out.println("inside false greater > 500"); csv_index++; if
	 * (csv_index < datalist.size()) { search_field_editbox.clear();
	 * Set_Search_keyword(datalist.get(csv_index).getKeyword(), driver); try {
	 * Thread.sleep(5000); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * }
	 * 
	 * } }
	 */

	public static void isScrollEnd() {

	}

	public static void Scroll() {

		WebElement list = driver.findElement(By.id("list"));
		TouchActions flick = new TouchActions(driver).flick(list, 0, -350, 0);
		flick.perform();

	}

	public static void Set_Search_keyword(String keyword, WebDriver driver)

	{
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		search_field_editbox = driver.findElement(By
				.id(ConstantID.Search_Editbox_ID));
		search_field_editbox.sendKeys(keyword);
	}

	public static boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static void Invite_User() throws FileNotFoundException {
		for (int i = 1; i <= ConstantID.list_size; i++) {

			String button_xpath = "//ListView/RelativeLayout[" + i
					+ "]/FrameLayout/AnimatedActionImageView";

			String name_xpath = "//ListView/RelativeLayout[" + i
					+ "]/RelativeLayout/TextView[1]";
			String name = driver.findElement(By.xpath(name_xpath)).getText()
					.toString();
			if (isElementPresent(By.xpath(button_xpath))) {

				add_button = driver.findElement(By.xpath(button_xpath));

				if (add_button.isDisplayed() == true) {

					if (!person_list.contains(name)) {

						Utilss.writeDataInText(usercsv, name);

						add_button.click();

						invite_present_count++;

						System.out.println("Total Invite Count="
								+ invite_present_count);
						invite_not_present_count = 0;
					} else {
						System.out.println("Person already invited Hell Yeah");
					}
				} else {
					invite_not_present_count++;
				}
				// for sending 250 request
				if (invite_present_count > 800) {
					invite_present_count = 0;
					invite_not_present_count = 0;
					System.out.println("251 request sent");
					csv_index++;
					if (csv_index < datalist.size()) {
						search_field_editbox.clear();
						Set_Search_keyword(
								datalist.get(csv_index).getKeyword(), driver);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				// for sending 250 request

				if (invite_not_present_count > 500) {
					invite_not_present_count = 0;
					System.out.println("inside false greater > 500");
					csv_index++;
					if (csv_index < datalist.size()) {
						search_field_editbox.clear();
						Set_Search_keyword(
								datalist.get(csv_index).getKeyword(), driver);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			} else {

				System.out.println("Inside Profile click");
				new Actions(driver).sendKeys(SelendroidKeys.BACK).perform();
			}

		}
	}
}
