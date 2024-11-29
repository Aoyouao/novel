package com.aoyouao.novel.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 作者信息
 * </p>
 *
 * @author ${author}
 * @date 2024/11/23
 */
@TableName("author_info")
    public class AuthorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 主键
         */
                @TableId(value = "id", type = IdType.AUTO)
                private Long id;

        /**
         * 用户ID
         */
        private Long userId;

        /**
         * 笔名
         */
        private String penName;

        /**
         * 手机号码
         */
        private String telPhone;

        /**
         * QQ或微信号
         */
        private String chatAccount;

        /**
         * 电子邮箱
         */
        private String email;

        /**
         * 0-正常 1-封禁
         */
        private Integer status;


        public Long getId() {
        return id;
    }

            public void setId(Long id) {
        this.id = id;
    }

        public Long getUserId() {
        return userId;
    }

            public void setUserId(Long userId) {
        this.userId = userId;
    }

        public String getPenName() {
        return penName;
    }

            public void setPenName(String penName) {
        this.penName = penName;
    }

        public String getTelPhone() {
        return telPhone;
    }

            public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

        public String getChatAccount() {
        return chatAccount;
    }

            public void setChatAccount(String chatAccount) {
        this.chatAccount = chatAccount;
    }

        public String getEmail() {
        return email;
    }

            public void setEmail(String email) {
        this.email = email;
    }

        public Integer getStatus() {
        return status;
    }

            public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
    return "AuthorInfo{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", penName=" + penName +
                    ", telPhone=" + telPhone +
                    ", chatAccount=" + chatAccount +
                    ", email=" + email +
                    ", status=" + status +
            "}";
}
}
