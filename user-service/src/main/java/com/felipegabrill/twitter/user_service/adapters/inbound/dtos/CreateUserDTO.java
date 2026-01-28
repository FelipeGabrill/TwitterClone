package com.felipegabrill.twitter.user_service.adapters.inbound.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class CreateUserDTO {

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 15, message = "Username deve ter entre 3 e 15 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username deve conter apenas letras, números ou underscore"
    )
    private String username;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 50, message = "Nome pode ter no máximo 50 caracteres")
    private String name;

    @Size(max = 160, message = "Bio pode ter no máximo 160 caracteres")
    private String bio;

    private MultipartFile profileImage;

    private MultipartFile bannerImage;

    @Size(max = 100, message = "Localização pode ter no máximo 100 caracteres")
    private String location;

    public CreateUserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
