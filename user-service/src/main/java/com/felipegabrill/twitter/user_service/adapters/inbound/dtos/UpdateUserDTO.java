package com.felipegabrill.twitter.user_service.adapters.inbound.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@Schema(
        name = "UpdateUserDTO",
        description = "DTO used to partially update user profile information"
)
public class UpdateUserDTO {

    @Schema(
            description = "User display name",
            example = "John Doe"
    )
    @Size(max = 50, message = "Nome pode ter no máximo 50 caracteres")
    private String name;

    @Schema(
            description = "Short biography displayed on the profile",
            example = "Software engineer | Coffee lover ☕"
    )
    @Size(max = 160, message = "Bio pode ter no máximo 160 caracteres")
    private String bio;

    @Schema(
            description = "User location",
            example = "São Paulo, Brazil"
    )
    @Size(max = 100, message = "Localização pode ter no máximo 100 caracteres")
    private String location;

    @Schema(
            description = "Profile image file",
            type = "string",
            format = "binary"
    )
    private MultipartFile profileImage;

    @Schema(
            description = "Banner image file",
            type = "string",
            format = "binary"
    )
    private MultipartFile bannerImage;

    @Schema(
            description = "Indicates whether the profile image should be removed",
            example = "false"
    )
    private Boolean isRemoveProfileImage;

    @Schema(
            description = "Indicates whether the banner image should be removed",
            example = "false"
    )
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
