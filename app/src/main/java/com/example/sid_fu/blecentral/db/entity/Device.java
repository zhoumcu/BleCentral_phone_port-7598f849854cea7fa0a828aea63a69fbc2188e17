package com.example.sid_fu.blecentral.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "tb_device")
public class Device implements Serializable
{
	private static final long serialVersionUID = -7060210544600464481L;

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String deviceName;
	@DatabaseField
	private String deviceDescripe;
	@DatabaseField
	private String left_FD;
	@DatabaseField
	private String right_FD;
	@DatabaseField
	private String left_BD;
	@DatabaseField
	private String right_BD;

	@DatabaseField
	private String imagePath;

	@DatabaseField(canBeNull = true, foreign = true, columnName = "user_id", foreignAutoRefresh = true)
	private User user;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceDescripe() {
		return deviceDescripe;
	}

	public void setDeviceDescripe(String deviceDescripe) {
		this.deviceDescripe = deviceDescripe;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	@DatabaseField(columnName = "deviceID")
	private String deviceID;
	@DatabaseField(columnName = "carID")
	private int carID;

	public String getDefult() {
		return isDefult;
	}

	public void setDefult(String defult) {
		isDefult = defult;
	}

	@DatabaseField(columnName = "isDefult")
	private String isDefult = "false";

	public String getLeft_FD() {
		return left_FD;
	}

	public void setLeft_FD(String left_FD) {
		this.left_FD = left_FD;
	}

	public String getRight_FD() {
		return right_FD;
	}

	public void setRight_FD(String right_FD) {
		this.right_FD = right_FD;
	}

	public String getLeft_BD() {
		return left_BD;
	}

	public void setLeft_BD(String left_BD) {
		this.left_BD = left_BD;
	}

	public String getRight_BD() {
		return right_BD;
	}

	public void setRight_BD(String right_BD) {
		this.right_BD = right_BD;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@DatabaseField(columnName = "fromMinValues")
	private String fromMinValues;
	@DatabaseField(columnName = "fromMaxValues")
	private String fromMaxValues;
	@DatabaseField(columnName = "fromMidValues")
	private String fromMidValues;

	public String getIsDefult() {
		return isDefult;
	}

	public void setIsDefult(String isDefult) {
		this.isDefult = isDefult;
	}

	public String getFromMinValues() {
		return fromMinValues;
	}

	public void setFromMinValues(String fromMinValues) {
		this.fromMinValues = fromMinValues;
	}

	public String getFromMaxValues() {
		return fromMaxValues;
	}

	public void setFromMaxValues(String fromMaxValues) {
		this.fromMaxValues = fromMaxValues;
	}

	public String getFromMidValues() {
		return fromMidValues;
	}

	public void setFromMidValues(String fromMidValues) {
		this.fromMidValues = fromMidValues;
	}

	public String getBackMinValues() {
		return backMinValues;
	}

	public void setBackMinValues(String backMinValues) {
		this.backMinValues = backMinValues;
	}

	public String getBackmMaxValues() {
		return backmMaxValues;
	}

	public void setBackmMaxValues(String backmMaxValues) {
		this.backmMaxValues = backmMaxValues;
	}

	public String getBackMidValues() {
		return backMidValues;
	}

	public void setBackMidValues(String backMidValues) {
		this.backMidValues = backMidValues;
	}

	@DatabaseField(columnName = "backMinValues")
	private String backMinValues;
	@DatabaseField(columnName = "backmMaxValues")
	private String backmMaxValues;
	@DatabaseField(columnName = "backMidValues")
	private String backMidValues;

	public String getIsShare() {
		return isShare;
	}

	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}

	@DatabaseField(columnName = "isShare")
	private String isShare = "false";
	@Override
	public String toString()
	{
		return "Article [id=" + id
				+ ", user=" + user
				+ ", left_BD=" + left_BD
				+", right_BD="+ right_BD
				+", left_FD="+left_FD
				+", right_FD="+right_FD
				+", isDefult="+isDefult
				+", isShare="+isShare
				+", imagePath="+imagePath
				+", backMinValues="+backMinValues
				+", backmMaxValues="+backmMaxValues
				+", backMidValues="+backMidValues
				+", fromMinValues="+fromMinValues
				+", fromMaxValues="+fromMaxValues
				+", fromMidValues="+fromMidValues
				+ "]";
	}

}
