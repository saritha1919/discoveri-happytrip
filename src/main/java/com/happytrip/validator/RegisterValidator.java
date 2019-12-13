package com.happytrip.validator;

import java.util.Calendar;
import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.happytrip.controllers.dto.UserDto;
import com.happytrip.exception.InvalidDateOfBirthException;
import com.happytrip.exception.InvalidEmailException;
import com.happytrip.model.User;

public class RegisterValidator implements Validator {
	@Override
	public boolean supports(Class clazz) {
		return User.class.isAssignableFrom(clazz);

	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,
				"email", "required.email",
				"Email is mandatory");
		
	ValidationUtils.rejectIfEmptyOrWhitespace(errors,
				"fullName","required.fullName","name is reQUIRED");//for validating full name field
		

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
				"required.password", "Password is mandatory");
				
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth",
				"required.dateOfBirth", "Date of birth is mandatory");
		
		/* code added for CR3.4-Registration-validate-exception
		 */
		//added code starts here for CR4-Registration-validate-exception
		
		  UserDto userDto=(UserDto)target;
		  try{
		  if(!(isValidEmailAddress(userDto.getEmail()))){
		   errors.rejectValue("email","UserDto.email.required","invalid email");
		   throw new InvalidEmailException();
		 }
		  if(!(isValiddateOfBirth(userDto.getDateOfBirth()))){
		   errors.rejectValue("dateOfBirth", "UserDto.email.dateOfBirth", "FUTURE DATE IS NOT ALLOWED");
		   throw new InvalidDateOfBirthException("FUTURE DATE IS NOT ALLOWED");
		  }
		  }catch(Exception e){
		   System.out.println(e.getMessage());
		  }
		//added code ends here for CR4-Registration-validate-exception 

	}
	/* code added for CR4-Registration-validate-exception
	 */
	//added code starts here for CR 3.4-Registration-validate-exception
	public boolean isValiddateOfBirth(Date dob){
		  
		  Calendar cal1 = Calendar.getInstance();
		     Calendar cal2 = Calendar.getInstance();
		     cal1.setTime(new Date());
		     cal2.setTime(dob);
		     if(cal2.after(cal1)){
		      return false;
		     }
		     else{
		      return true;
		     }
		 }
	
	public boolean isValidEmailAddress(String email) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(".+@.+\\.[a-z]+");
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }
	
	//added code ends here for CR4-Registration-validate-exception
}
