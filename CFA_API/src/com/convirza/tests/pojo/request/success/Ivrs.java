package com.convirza.tests.pojo.request.success;

public class Ivrs {
	
	 private int Key;
	 private String name;
	 private boolean message_enabled;
	 private String message;
	 private String route_type;
	 private boolean record_enabled;
	 private String play_disclaimer;
	 private String target_did;


	 // Getter Methods 

	 public int getKey() {
	  return Key;
	 }

	 public String getName() {
	  return name;
	 }

	 public boolean getMessage_enabled() {
	  return message_enabled;
	 }

	 public String getMessage() {
	  return message;
	 }

	 public String getRoute_type() {
	  return route_type;
	 }

	 public boolean getRecord_enabled() {
	  return record_enabled;
	 }

	 public String getPlay_disclaimer() {
	  return play_disclaimer;
	 }

	 public String getTarget_did() {
	  return target_did;
	 }

	 // Setter Methods 

	 public void setKey(int Key) {
	  this.Key = Key;
	 }

	 public void setName(String name) {
	  this.name = name;
	 }

	 public void setMessage_enabled(boolean message_enabled) {
	  this.message_enabled = message_enabled;
	 }

	 public void setMessage(String message) {
	  this.message = message;
	 }

	 public void setRoute_type(String route_type) {
	  this.route_type = route_type;
	 }

	 public void setRecord_enabled(boolean record_enabled) {
	  this.record_enabled = record_enabled;
	 }

	 public void setPlay_disclaimer(String play_disclaimer) {
	  this.play_disclaimer = play_disclaimer;
	 }

	 public void setTarget_did(String target_did) {
	  this.target_did = target_did;
	 }
	 
	 @Override
	public String toString() {
		return "Ivrs [Key=" + Key + ", name=" + name + ", message_enabled=" + message_enabled + ", message=" + message
				+ ", route_type=" + route_type + ", record_enabled=" + record_enabled + ", play_disclaimer="
				+ play_disclaimer + ", target_did=" + target_did + "]";
	}
	 
}
