package com.happytrip.util.transformer;

import com.happytrip.controllers.dto.ContactDto;
import com.happytrip.controllers.dto.UserDto;
import com.happytrip.model.User;
import com.happytrip.model.UserContact;

public final class UserDtoTransformer {

	private UserDtoTransformer() {
	}

	public static User transform(UserDto userDto) {
		User user = new User();
		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setEmail(userDto.getEmail());
		user.setEnabled(userDto.isEnabled());
		user.setLoginId(userDto.getLoginId());
		user.setPassword(userDto.getPassword());
		user.setUserId(userDto.getUserId());
		
		/*
		 * code modified for mock bug 2.3
		 */
		//modified code starts here for mock bug 2.3
		user.setFullName(userDto.getFullName());
		//modified code ends here for mock bug 2.3
		
		/*
		 * code modified for mock bug 1.3
		 */
		//modified code starts here for mock bug 1.3
		user.setGender(userDto.getGender());
		//modified code ends here for mock bug 1.3
		
		UserContact contact = transform(userDto.getUserContact());
		if (contact != null) {
			contact.setUser(user);
			user.setUserContact(contact);
		}
		return user;
	}

	public static UserContact transform(ContactDto contactDto) {
		if (contactDto != null) {
			UserContact contact = new UserContact();
			contact.setUserId(contactDto.getUserId());
			contact.setAddress(contactDto.getAddress());
			contact.setMobileNo(contactDto.getMobileNo());
			contact.setPinCode(contactDto.getPinCode());
			contact.setCity(contactDto.getCity()); //added code
			return contact;
		} else {
			return null;
		}
	}
}
