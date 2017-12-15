package com.boulder.mchistory.basic;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boulder.mchistory.auth.LoginEmail;


public class FieldValidator {
	
	private static final Logger logger = Logger.getLogger(LoginEmail.class.getName());


	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

		public static boolean validEmail(String email) {
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
		}

		public static boolean validPass(String pass) {
			  Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
			  Matcher matcher = pattern.matcher(pass);
			  return matcher.matches();
		  }


}
