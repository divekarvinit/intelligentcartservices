package com.iot.DO;

public class ImageMaster {

	private String imageMasterId;
	private String imageFileName;
	private String imagePath;
	private String imageName;
	private byte[] imageFile;
	
	public byte[] getImageFile() {
		return imageFile;
	}
	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}
	public ImageMaster() {
		super();
	}
	public ImageMaster(String imageMasterId, String imageFileName, String imagePath, String imageName,
			byte[] imageFile) {
		super();
		this.imageMasterId = imageMasterId;
		this.imageFileName = imageFileName;
		this.imagePath = imagePath;
		this.imageName = imageName;
		this.imageFile = imageFile;
	}
	public String getImageMasterId() {
		return imageMasterId;
	}
	public void setImageMasterId(String imageMasterId) {
		this.imageMasterId = imageMasterId;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
