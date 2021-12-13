package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.User;
import com.LoRaWan.LoRaWan.Date.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository  extends JpaRepository<User, Integer> {


     @Transactional
     @Query(value = "select * from user where email=:Email",nativeQuery = true)
     public User getUserByEmail(@Param("Email") String email);

     @Modifying
     @Transactional
     @Query(value = "insert into user (first_name,last_name,user_name,email,password)\n" +
             "select :FirstName, :LastName , :UserName , :Email , :Parola from user where not exists(select * from user where email=:Email )",nativeQuery = true)
     void insertNewUser(@Param("FirstName")String firstName,
                        @Param("LastName")String lastName,
                        @Param("Email")String email,
                        @Param("UserName")String userName,
                        @Param("Parola")String parola);




     @Transactional
     @Query(value = "select user.user_name as name,user.first_name as fn, user.last_name as ln, user.email as email ,rol.rol as rol  from user,rol where user.email=:Email and user.id=rol.user_id;",nativeQuery = true)
     UserInfo userInfo(@Param("Email") String email);


}
