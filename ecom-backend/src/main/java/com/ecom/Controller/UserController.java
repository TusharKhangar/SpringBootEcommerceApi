package com.ecom.Controller;

import com.ecom.Security.JwtHelper;
import com.ecom.Services.UserService;
import com.ecom.payload.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ModelMapper mapper;

    @Autowired
    private JwtHelper helper;
	@Autowired
	private UserService userService;
	 
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto){
		if (userService.userExist(userDto)) {
			return ResponseEntity.badRequest().body("Error : username is already taken");
		}else{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date=new Date();
			formatter.format(date);
			userDto.setDate(date);
			userDto.setActive(true);
			UserDto ud=this.userService.create(userDto);
		return new ResponseEntity<>(ud,HttpStatus.CREATED);
		}
		//creating new account
	}
	@GetMapping("/getUser/{userId}")
	@ResponseStatus(HttpStatus.FOUND)
	public UserDto getUserById(@PathVariable int userId) {
		return  userService.getByUserId(userId);
	}
	@DeleteMapping("/deleteUser/{userId}")
	public void deleteUser(@PathVariable int userId) {
		this.userService.delete(userId);
	}

	@GetMapping("/getUsers")
	@ResponseBody
	@ResponseStatus(HttpStatus.FOUND)
	public List<UserDto> getUsers() {
		return userService.getAll();
	}

	@PutMapping("/updateUser/{userId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.ACCEPTED)
	public UserDto updateUser(@PathVariable int userId, @RequestBody UserDto userDto) {
		return userService.update(userDto,userId);
	}


}
	
	
	