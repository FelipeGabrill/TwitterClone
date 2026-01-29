package com.felipegabrill.twitter.user_service.adapters.inbound.dtos;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class UpdateUserDTO {

    @Size(max = 50, message = "Nome pode ter no máximo 50 caracteres")
    private String name;

    @Size(max = 160, message = "Bio pode ter no máximo 160 caracteres")
    private String bio;

    @Size(max = 100, message = "Localização pode ter no máximo 100 caracteres")
    private String location;

    private MultipartFile profileImage;
    private MultipartFile bannerImage;

    private Boolean isRemoveProfileImage;
    private Boolean isRemoveBannerImage;

    public UpdateUserDTO() {
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public MultipartFile getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(MultipartFile bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getRemoveProfileImage() {
        return isRemoveProfileImage;
    }

    public void setRemoveProfileImage(Boolean removeProfileImage) {
        isRemoveProfileImage = removeProfileImage;
    }

    public Boolean getRemoveBannerImage() {
        return isRemoveBannerImage;
    }

    public void setRemoveBannerImage(Boolean removeBannerImage) {
        isRemoveBannerImage = removeBannerImage;
    }
}
