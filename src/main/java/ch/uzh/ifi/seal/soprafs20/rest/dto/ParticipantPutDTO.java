package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserState;

public class ParticipantPutDTO{

        private String token;
        
        private UserState userState;
        	
        public void setUserState(UserState userState) {
        	this.userState = userState;
        }
        
        public UserState getUserState() {
        	return userState;
        }
        
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
}

