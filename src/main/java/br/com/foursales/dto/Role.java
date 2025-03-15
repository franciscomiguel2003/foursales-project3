package br.com.foursales.dto;

public enum Role {
    ADMIN("ADMIN"), USER("USER");

    String role;
    private Role(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }

}