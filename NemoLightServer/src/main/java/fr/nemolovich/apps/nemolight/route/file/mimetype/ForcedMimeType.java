/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.route.file.mimetype;

/**
 *
 * @author Nemolovich
 */
public abstract class ForcedMimeType {

	private String extension;
	private String mimeType;

	public ForcedMimeType(String extension, String mimeType) {
		this.extension = extension;
		this.mimeType = mimeType;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public static ForcedMimeType newInstance(String extension,
		String mimeType) {
		return new ForcedMimeType(extension, mimeType) {
		};
	}
}
