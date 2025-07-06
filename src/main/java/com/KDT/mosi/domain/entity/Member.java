package com.KDT.mosi.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
  private Long memberId;
  private String nickname;
  private String email;
  private String passwd;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
}
