package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserState;

public class ParticipantPutDTO{

        private String token;
        
        private UserState userState;
        	
        public void setState(UserState userState) {
        	this.userState = userState;
        }
        
        public UserState getState() {
        	return userState;
        }
        
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
}

