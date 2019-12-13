package com.happytrip.util.transformer;

import com.happytrip.controllers.dto.ContactDto;
import com.happytrip.controllers.dto.UserDto;
import com.happytrip.model.User;
import com.happytrip.model.UserContact;
import com.happytrip.model.reports.UserReport;

public final class UserModelTransformer {

	private UserModelTransformer(){}
	
	public static UserDto transform(User user) {
		UserDto userDto = new UserDto();
		userDto.setDateOfBirth(user.getDateOfBirth());
		userDto.setEmail(user.getEmail());
		userDto.setEnabled(user.isEnabled());
		userDto.setLoginId(user.getLoginId());
		userDto.setPassword(user.getPassword());
		userDto.setUserId(user.getUserId());
		
		/*
		 * code modified for mock bug 2.3
		 */
		//modified code starts here for mock bug 2.3
		userDto.setFullName(user.getFullName());//added code
		//modified code ends here for mock bug 2.3

		ContactDto contactDto = transform(user.getUserContact());
		if (contactDto != null) {
			contactDto.setUser(userDto);
			userDto.setUserContact(contactDto);
		}
		return userDto;
	}

	public static UserReport transformToUserReport(User user)
	{
		UserReport userReport = new UserReport();
		userReport.setEmail(user.getEmail());
		userReport.setFullName(user.getFullName());
		userReport.setGender(user.getGender());
		return userReport;
	}
	public static ContactDto transform(UserContact contactModel) {
		if (contactModel != null) {
			ContactDto contactDto = new ContactDto();
			contactDto.setAddress(contactModel.getAddress());
			contactDto.setCity(contactModel.getCity());
			contactDto.setMobileNo(contactModel.getMobileNo());
			contactDto.setPinCode(contactModel.getPinCode());
			return contactDto;
		} else {
			return null;
		}
	}
}
