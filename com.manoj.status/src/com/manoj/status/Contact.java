package com.manoj.status;

import android.graphics.Bitmap;

public class Contact {
private String name;
private String email;
private String number;
private String status;
private String statusUpdated;
private String photoUpdated;
private Bitmap photoFull;
private Bitmap photoThumb;

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getNumber() {
	return number;
}
public void setNumber(String number) {
	this.number = number;
}
public String getStatusUpdated() {
	return statusUpdated;
}
public void setStatusUpdated(String statusUpdated) {
	this.statusUpdated = statusUpdated;
}
public String getPhotoUpdated() {
	return photoUpdated;
}
public void setPhotoUpdated(String photoUpdated) {
	this.photoUpdated = photoUpdated;
}
public Bitmap getPhotoFull() {
	return photoFull;
}
public void setPhotoFull(Bitmap photoFull) {
	this.photoFull = photoFull;
}
public Bitmap getPhotoThumb() {
	return photoThumb;
}
public void setPhotoThumb(Bitmap photoThumb) {
	this.photoThumb = photoThumb;
}


public Contact(String name,String number)
{
	this.name = name;
	this.number = number;
}

public Contact(String name,String number,String email,String status,String statusupdated,String photoupdated)
{
	this.name = name;
	this.number = number;
	this.email = email;
	this.status = status;
	this.statusUpdated = statusupdated;
	this.photoUpdated = photoupdated;
}

public Contact(String number)
{
	this.number = number;
}

}
