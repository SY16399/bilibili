package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {
    private int id;
    private String name;
    private String nickname;//
    private String password;
    private String email;//
    private String phone;//
    private String avatar;
    private int roleId;
    private int gender;//
    private String status;
    private int regTime;
    private LocalDate birthday;//生日
    private String desc;//个人介绍
}