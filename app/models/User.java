package models;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import play.libs.Mail;
import play.mvc.*;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;


import models.*;

@Entity
public class User extends Model{
	
	@Required
	@Unique
	public String username;
	
	@Required
	@Email
	@Unique
	public String email;
	
	@Required
	public String name;
	
	@Required
	public String password;
	
	@ManyToMany(mappedBy="users", cascade=CascadeType.ALL)
	public List<Squad> squads;
	
	@ManyToMany(mappedBy="admins", cascade=CascadeType.ALL)
	public List<Squad> adminSquads;
	
	public String emailConfirmationToken = Long.toString(new Random().nextLong(), 16);
	
	
	public boolean confirmEmail(String token){
		if(token == this.emailConfirmationToken){
			return false;
		}
		else{
			this.emailConfirmationToken = null;
			this._save();
			return true;
		}
	}

	public void applyForSquad(Squad squad) {
		squad.usersApplying.add(this);
	}
	
	public void confirmRequest(Squad squad){
		squad.users.add(this);
		squad.usersApplying.remove(this);
	}
	
	public void denyRequest(Squad squad){
		squad.usersApplying.remove(this);
	}
	
}
