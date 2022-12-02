package com.misiontic.grupo17.securityBackend.services;

import com.misiontic.grupo17.securityBackend.models.User;
import com.misiontic.grupo17.securityBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
/**
 *
 */
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @return
     */
    public List<User> index(){
        List<User> resultList = (List<User>) this.userRepository.findAll();
        if (resultList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is not any user in the list.");
        return  resultList;

    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<User> show(int id){
        Optional<User> result = this.userRepository.findById(id);
        if(result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The requested user.id does not exist.");
        return  result;
    }

    /**
     *
     * @param email
     * @return
     */
    public Optional<User> showByEmail(String email){
        Optional<User> result = this.userRepository.findByEmail(email);
        if(result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The requested user.email does not exists.");
        return  result;
    }

    /**
     *
     * @param nickname
     * @return
     */
    public Optional<User> showByNickname(String nickname){
        Optional<User> result = this.userRepository.findByNickname(nickname);
        if(result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The requested user.nickname does not exists.");
        return  result;
    }

    /**
     *
     * @param newUser
     * @return
     */
    public ResponseEntity<User> create(User newUser){
        if(newUser.getId() != null){
            Optional<User> tempUser = this. userRepository.findById(newUser.getId());
            if(tempUser.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "ID is yet in the database.");
        }
        Optional<User> userEmail = this.userRepository.findByEmail(newUser.getEmail());

        if(userEmail.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There is an user with the same email.");

            if((newUser.getEmail() != null) && (newUser.getNickname() != null) &&
              (newUser.getPassword() != null) && (newUser.getRol() != null)){
                newUser.setPassword(this.convertToSHA256(newUser.getPassword()));
                return new ResponseEntity<>(this.userRepository.save(newUser), HttpStatus.CREATED);
            }else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Mandatory fields had not been provided.");
    }

    /**
     *
     * @param id
     * @param user
     * @return
     */
    public  ResponseEntity<User> update(int id, User user){
        if(id > 0){
            Optional<User> tempUser = this.userRepository.findById(id);
            if(tempUser.isPresent()){
                if(user.getNickname() != null)
                    tempUser.get().setNickname(user.getNickname());
                if (user.getPassword() != null)
                    tempUser.get().setPassword(this.convertToSHA256(user.getPassword()));
                if(user.getRol() != null)
                    tempUser.get().setRol(user.getRol());
                return new ResponseEntity<>(this.userRepository.save(tempUser.get()), HttpStatus.CREATED);
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User.id does not exist in database.");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User.id cannot be negative.");
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<Boolean> delete(int id) {
        Boolean success = this.show(id).map( user -> {
            this.userRepository.delete(user);
            return true;
         }).orElse(false);
        if(success)
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "User cannot be deleted.");
    }

    public User login(User user){
        User result;
        if((user.getPassword() != null) && (user.getEmail() != null)){
            String email = user.getEmail();
            String password = this.convertToSHA256(user.getPassword());
            Optional<User> tempUser = this.userRepository.validateLogin(email, password);
            if(tempUser.isEmpty())
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Invalid login.");
            else
                result = tempUser.get();
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Mandatory fields had not been provided.");
        return result;
    }


    /**
     *
     * @param password
     * @return
     */
    public String convertToSHA256(String password){
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        StringBuffer sb = new StringBuffer();
        byte[] hash = md.digest(password.getBytes());
        for(byte b: hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
