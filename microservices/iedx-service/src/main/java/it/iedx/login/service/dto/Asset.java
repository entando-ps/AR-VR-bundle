package it.iedx.login.service.dto;

import it.iedx.login.domain.AssetType;

public class Asset {

    public String id;
    public AssetType type;
    public String assetFilename;
    public String thumbnailFilename;
    public boolean assetPresentOnDisk;
    public boolean thumbnailPresentOnDisk;
    private String title;

    @Override
    public Asset clone() {
        Asset copy = new Asset();

        copy.setAssetFilename(assetFilename);
        copy.setId(id);
        copy.setTitle(title);
        copy.setType(type);
        copy.setThumbnailFilename(thumbnailFilename);
        copy.setAssetPresentOnDisk(assetPresentOnDisk);
        copy.setThumbnailFilename(thumbnailFilename);
        return copy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public String getAssetFilename() {
        return assetFilename;
    }

    public void setAssetFilename(String assetFilename) {
        this.assetFilename = assetFilename;
    }

    public Boolean getAssetPresentOnDisk() {
        return assetPresentOnDisk;
    }

    public void setAssetPresentOnDisk(Boolean assetPresentOnDisk) {
        this.assetPresentOnDisk = assetPresentOnDisk;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailFilename() {
        return thumbnailFilename;
    }

    public void setThumbnailFilename(String thumbnailFilename) {
        this.thumbnailFilename = thumbnailFilename;
    }

    public Boolean getThumbnailPresentOnDisk() {
        return thumbnailPresentOnDisk;
    }

    public void setThumbnailPresentOnDisk(Boolean thumbnailPresentOnDisk) {
        this.thumbnailPresentOnDisk = thumbnailPresentOnDisk;
    }

    public String toString() {
        return "[id " + id + " title: " + title + " asset filename: " + assetFilename + " thumbnail filename: "+ thumbnailFilename + " type: " + type + " asset present: " + assetPresentOnDisk + " thumbnail present:" + thumbnailPresentOnDisk + "]";
    }
}
