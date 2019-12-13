package com.happytrip.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.happytrip.controllers.dto.UserDto;
import com.happytrip.services.UserProfileService;
import com.happytrip.services.impl.UserProfileServiceImpl;
import com.happytrip.util.StringUtil;
import com.happytrip.util.transformer.UserDtoTransformer;
import com.happytrip.validator.RegisterValidator;

@Controller
public class RegisterController {

	private UserProfileService userProfileService;

	public RegisterController() {
		userProfileService = new UserProfileServiceImpl();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(false);

		// true passed to CustomDateEditor constructor means convert empty
		// String to null
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));

	}

	@ModelAttribute("UserRegister")
	public UserDto getRegisterForm() {
		return new UserDto();
	}

	@RequestMapping(value = "/register.html", method = RequestMethod.GET)
	public String register(ModelMap model) {

		return "register";
	}

	@RequestMapping(value = "/registerprocess", method = RequestMethod.POST)
	public String loginProcess(
			@ModelAttribute("UserRegister") UserDto userRegister,
			BindingResult result, ModelMap model) throws IOException {
		/*
		 * code modified for File Handling_CR_07.1
		 */
		// modified code starts here for File Handling_CR_07.1
		if (StringUtil.getAllString(" ", "data/blockedemails.txt").contains(userRegister.getEmail())) {
			result.rejectValue("email","UserDto.email.required","Blocked Email");
			return "register";
		}
		// modified code ends here for File Handling_CR_07.1

		/*
		 * code added for CR4-Registration-validate-exception
		 */
		// added code starts here for CR4-Registration-validate-exception
		RegisterValidator registerValidator = new RegisterValidator();
		registerValidator.validate(userRegister, result);

		// added code ends here for CR4-Registration-validate-exception
		if (result.hasErrors()) {
			return "register";
		}
		userProfileService.createUser(UserDtoTransformer
				.transform(userRegister));
		return "login";
	}

}
